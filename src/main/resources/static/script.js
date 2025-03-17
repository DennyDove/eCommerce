// Периодически приходится мучиться с тем, что Intellij или браузер
// не обновляет файл скрипта script.js
// При загрузке страницы на localhost потом нажать Ctrl + F5 --> и это должно решить проблему

let name = document.getElementById("name");
let age = document.getElementById("age");
let password = document.getElementById("password");
let regButton = document.getElementById("regButton");

async function createUser() {

  let obj = {
    name : name.value,
    age : age.value,
    password : password.value
  };

  let request = await fetch("/users",
  // Если указать путь URI --> "https", то будет выскакивать ошибка Failed to load resource: net::ERR_SSL_PROTOCOL_ERROR
    {
      method: 'POST',
      headers: {"Content-Type" : "application/json"},
      body: JSON.stringify(obj)
    });

  if(request.ok) {
    alert("User created!");
    window.location.replace("index.html");

  } else {
    alert("HTTP error: "+ request.status);
  }
}

regButton.addEventListener("click", function() {
    createUser();
});


async function deleteItem(prodId) {

  // простой вариант DELETE https запроса без тела. Здесь мы вручную вписали параметры в функцию fetch: fetch("/deleteitem?id="+ prodId.....
  let request = await fetch("/deleteitem?id=" + prodId, {
      method: 'POST'
  });

  if(request.ok) {
    //alert("Монета удалена из корзины");
    window.location.replace("/cart");
  } else {
    alert("HTTP error: "+ request.status);
  }

  /* вариант DELETE https запроса c телом запроса и объектом класса URLSearchParams.
  var params = new URLSearchParams('id=' + prodId);
  let request = await fetch("/deleteitem", {
      method: 'DELETE',
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"},
      body: params
  });
  */
}

buyButton.addEventListener("click", function() {
    buyCoin();
});

/*
function priceWithDelim() {
	$('#price').text().toLocaleString());
};

$('#price')
  .change(priceWithDelim)
  .change();
*/
