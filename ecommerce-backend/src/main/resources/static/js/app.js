// -------------------------
// Products
// -------------------------
const productTable = document.getElementById("productTable");
const productForm = document.getElementById("productForm");

function loadProducts() {
  fetch("/api/products")
    .then(res => res.json())
    .then(products => {
      productTable.innerHTML = "";
      products.forEach(p => {
        productTable.innerHTML += `
          <tr>
            <td>${p.id}</td>
            <td>${p.name}</td>
            <td>${p.description}</td>
            <td>${p.price}</td>
            <td>${p.stock}</td>
          </tr>
        `;
      });
    });
}

if (productForm) {
  productForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const data = {
      name: document.getElementById("pName").value,
      description: document.getElementById("pDesc").value,
      price: parseFloat(document.getElementById("pPrice").value),
      stock: parseInt(document.getElementById("pStock").value)
    };
    fetch("/api/products", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    })
    .then(res => res.json())
    .then(() => {
      loadProducts();
      productForm.reset();
    });
  });
}

// -------------------------
// Customers
// -------------------------
const customerTable = document.getElementById("customerTable");
const customerForm = document.getElementById("customerForm");

function loadCustomers() {
  fetch("/api/customers")
    .then(res => res.json())
    .then(customers => {
      customerTable.innerHTML = "";
      customers.forEach(c => {
        customerTable.innerHTML += `
          <tr>
            <td>${c.id}</td>
            <td>${c.name}</td>
            <td>${c.email}</td>
            <td>${c.address}</td>
          </tr>
        `;
      });
    });
}

if (customerForm) {
  customerForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const data = {
      name: document.getElementById("cName").value,
      email: document.getElementById("cEmail").value,
      address: document.getElementById("cAddress").value
    };
    fetch("/api/customers", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    })
    .then(res => res.json())
    .then(() => {
      loadCustomers();
      customerForm.reset();
    });
  });
}

// -------------------------
// Orders
// -------------------------
const orderTable = document.getElementById("orderTable");
const orderForm = document.getElementById("orderForm");

function loadOrders() {
  fetch("/api/orders")
    .then(res => res.json())
    .then(orders => {
      orderTable.innerHTML = "";
      orders.forEach(o => {
        orderTable.innerHTML += `
          <tr>
            <td>${o.id}</td>
            <td>${o.customer.name}</td>
            <td>${o.product.name}</td>
            <td>${o.quantity}</td>
            <td>${o.totalPrice}</td>
            <td>${o.orderDate ? o.orderDate : ""}</td>
          </tr>
        `;
      });
    });
}

if (orderForm) {
  orderForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const data = {
      customer: { id: parseInt(document.getElementById("oCustomerId").value) },
      product: { id: parseInt(document.getElementById("oProductId").value) },
      quantity: parseInt(document.getElementById("oQuantity").value),
      totalPrice: parseInt(document.getElementById("oQuantity").value) *
                  parseFloat(document.getElementById("oProductPrice").value)
    };
    fetch("/api/orders", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    })
    .then(res => res.json())
    .then(() => {
      loadOrders();
      orderForm.reset();
    });
  });
}

// -------------------------
// Initial load
// -------------------------
document.addEventListener("DOMContentLoaded", () => {
  if(productTable) loadProducts();
  if(customerTable) loadCustomers();
  if(orderTable) loadOrders();
});
