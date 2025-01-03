function copyToClipboard(button, snippet_id) {
  const codeSnippet = document.getElementById(snippet_id).textContent;
  
  navigator.clipboard.writeText(codeSnippet).then(() => {
    // Change button label to "Copied!"
     button.innerHTML = '✅';

    
    // Change it back to "Copy" after 2 seconds
    setTimeout(() => {
     button.innerHTML ='📋';
    }, 2000);
  }).catch(err => {
    console.error('Failed to copy text: ', err);
    showMessage()
    alert('Failed to copy text. Please try again.');
  });
}

function downloadAsText(button) {
  const repoContent = document.getElementById('repo-content').textContent;
  const repoStructure = document.getElementById('repo-structure').textContent;

  // Create a Blob with the content
  const blob = new Blob([repoStructure, repoContent], { type: "text/plain" });

  // Create a link element
  const link = document.createElement("a");
  link.href = URL.createObjectURL(blob);
  link.download = "repo.txt"; // Set the filename

  // Trigger the download
  link.click();

  // Clean up the URL object
  URL.revokeObjectURL(link.href);
}

function parseGitRepo() {

  const parse_input = document.getElementById("parse_input").value;
  if (parse_input)
  console.log(parse_input)
  const apiUrl = `/api/v1/parse/`+ parse_input;

  const loadingOverlay = document.getElementById('loading-overlay');
  loadingOverlay.style.display = 'flex';

  // Fetching data from API
  fetch(apiUrl)
    .then(response => response.json())
    .then(data => {
     loadingOverlay.style.display = 'none';
      document.getElementById('repo-content').textContent = data.fileContents;
      document.getElementById('repo-structure').textContent = data.repoStructure;
      document.getElementById('repo-name').textContent = parse_input;
      document.getElementById('commit-time').textContent = "last commit on - " + data.lastCommitTime
      document.getElementById('main-content').style.display = 'block';
      document.getElementById('repo-structure').style.display = 'block';
    })
    .catch(error => {
      console.error('Error fetching data:', error);
       loadingOverlay.style.display = 'none';
       showMessage("Error, Check the repo!", "test");
//      alert('There was an error fetching the data. Please try again later.');
    })
}

function showMessage(message, messageType) {
  // Get the snackbar DIV
  var x = document.getElementById("snackbar");

  // Add the "show" class to DIV
  x.className = "show";
  x.textContent = message;
  // After 3 seconds, remove the show class from DIV
  setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}