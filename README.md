## gitparse
<img width="916" alt="image" src="https://github.com/user-attachments/assets/49e7ffb7-755b-4936-88c0-39c0501271bb" />


[gitparse.com](https://gitparse.com)

Convert any git repo to Prompt friendly text.

### API
API is work in progress
```
curl https://gitparse.com/api/v1/parse/https://github.com/samair/gitparse
```
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

