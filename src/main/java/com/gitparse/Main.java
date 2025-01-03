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

    public static Javalin createApp() {
        return createApp(new GitService());
    }

    public static Javalin createApp(GitService gitService) {

        return Javalin.create(javalinConfig -> {
                    javalinConfig.fileRenderer(new JavalinMustache());
                    javalinConfig.staticFiles.add("/public", Location.CLASSPATH);
                })
                .get("/", context ->
                        context.render("templates/template.html", model("name", "John")))
                .get("/parse/<repo>", ctx ->
                    {
                        NaiveRateLimit.requestPerTimeUnit(ctx,5, TimeUnit.MINUTES);
                        var repoName = ctx.pathParam("repo");
                        var resp = gitService.clone(ctx.pathParam("repo"));
                        ctx.render("templates/template.html",
                                model("name", repoName, "lastCommit", resp.getLastCommitTime(),
                                "fileStructure", resp.getRepoStructure(), "repoContent", resp.getFileContents()));
                    }
                )
                .get("/api/v1/parse/<repo>", context -> {
                    NaiveRateLimit.requestPerTimeUnit(context,5, TimeUnit.MINUTES);
                    var repoName = context.pathParam("repo");
                    context.json(context.jsonMapper().toJsonString(gitService.clone(repoName), ParseResponse.class));
                });

    }

    public static void main(String[] args) {
        createApp().start(7878);
    }
}