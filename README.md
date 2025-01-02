## gitparse
![img.png](img.png)

[gitparse.com](https://gitparse.com)

Convert any git repo to Prompt friendly text.

### Stack
- [Javalin](https://javalin.io/)
- [Mustache Java](https://github.com/spullara/mustache.java)
- [Google Analytics](https://developers.google.com/analytics)

### Run locally
1. Clone the repository
  ```bash
  git clone https://github.com/samair/gitparse.git
  ```
2. If you want to run a docker image
  ```bash
 ./gradlew buildDockerImage
  ```
  + Other wise, import the project into your favourite IDE and run the main method in 
  ```
  Main.java
  ```
3. Open http://localhost:7878 in your browser

