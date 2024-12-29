package com.gitparse;

import com.gitparse.scrape.GitService;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinMustache;

import static io.javalin.rendering.template.TemplateUtil.model;

public class Main {


    public static void main(String[] args) {

        GitService gitService = new GitService();
        System.out.println("Hello world!");
        Javalin.create(javalinConfig ->{
            javalinConfig.fileRenderer(new JavalinMustache());

                } )
                .get("/", context ->
                        context.render("templates/template.html", model("name", "John")))
                .get("/ping", context -> context.result("Pong!!"))
                .get("/clone/<repo>", ctx ->
                {
                    var repoName = ctx.pathParam("repo");
                    var resp = gitService.clone(ctx.pathParam("repo"));
                    ctx.render("templates/template.html", model("name", repoName, "lastCommit", resp.get(2),
                            "fileStructure", resp.get(0), "testCode", resp.get(1)));
                }

                )
                .start(7878);
    }
}