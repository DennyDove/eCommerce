
let login = document.getElementById("login");
let password = document.getElementById("password");
let loginButton = document.getElementById("loginButton");


async function authentication() {

  let obj = {
    login : login.value,
    password : password.value
  };

  let request = await fetch("/login",
  // Если указать путь URI --> "https", то будет выскакивать ошибка Failed to load resource: net::ERR_SSL_PROTOCOL_ERROR
    {
      method: 'POST',
      headers: {"Content-Type" : "application/json"},
      body: JSON.stringify(obj)
    });

  if(request.ok) {
    window.location.replace("/products");

  } else {
    alert("HTTP error: "+ request.status);
  }
}


loginButton.addEventListener("click", function() {
    authentication();
});
