package com.gitparse;

import com.gitparse.model.ParseResponse;
import com.gitparse.parse.GitService;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.http.util.NaiveRateLimit;
import io.javalin.rendering.template.JavalinMustache;

import java.util.concurrent.TimeUnit;

import static io.javalin.rendering.template.TemplateUtil.model;

public class Main {

    public static void main(String[] args) {


        Javalin.create(javalinConfig -> {
                    javalinConfig.fileRenderer(new JavalinMustache());
                    javalinConfig.staticFiles.add("/public", Location.CLASSPATH);
                })
                .get("/", context ->
                        context.render("templates/template.html", model("name", "John")))
                .get("/parse/<repo>", ctx ->
                    {
                        NaiveRateLimit.requestPerTimeUnit(ctx,5, TimeUnit.MINUTES);
                        var repoName = ctx.pathParam("repo");
                        GitService gitService = new GitService();
                        var resp = gitService.clone(ctx.pathParam("repo"));
                        ctx.render("templates/template.html",
                                model("name", repoName, "lastCommit", resp.getLastCommitTime(),
                                "fileStructure", resp.getRepoStructure(), "repoContent", resp.getFileContents()));
                    }
                )
                .get("/api/v1/parse/<repo>", context -> {
                    NaiveRateLimit.requestPerTimeUnit(context,5, TimeUnit.MINUTES);
                    GitService gitService = new GitService();
                    var repoName = context.pathParam("repo");
                    context.json(context.jsonMapper().toJsonString(gitService.clone(repoName), ParseResponse.class));
                })
                .start(7878);
    }
}