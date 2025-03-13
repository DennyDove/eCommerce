// Периодически приходится мучиться с тем, что Intellij или браузер
// не обновляет файл скрипта script.js
// При загрузке страницы на localhost потом нажать Ctrl + F5 --> и это должно решить проблему

let name = document.getElementById("name");
let age = document.getElementById("age");
let password = document.getElementById("password");
let regButton = document.getElementById("regButton");
let buyButton = document.getElementById("buyButton");
let price = document.getElementById("price");

let prodId = document.getElementById("prodId");
let prodQty = document.getElementById("prodQty");

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
/*
async function buyCoin() {

  let id = prodId.value;
  let quantity = prodQty.value;

  var id = $('#prodId').val();
  var quantity = $('#prodQty').val();

  var params = new URLSearchParams('id=' + id + '&quantity=' + quantity);

  let request = await fetch("/buycoin", {
      method: 'POST',
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"},
      body: params
  });

  if(request.ok) {
    alert("Монета добавлена в корзину");
  } else {
    alert("HTTP error: "+ request.status);
  }
}

buyButton.addEventListener("click", function() {
    buyCoin();
});
*/


regButton.addEventListener("click", function() {
    createUser();
});

/*
function priceWithDelim() {
	$('#price').text().toLocaleString());
};

$('#price')
  .change(priceWithDelim)
  .change();
*/
