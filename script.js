// ===== Mobile Menu Toggle =====
const menuBtn = document.getElementById('menu-btn');
const mobileNav = document.getElementById('mobile-nav');

menuBtn.addEventListener('click', () => {
  mobileNav.classList.toggle('open');
});

// ===== Cart Drawer =====
let cart = [];

const buyButtons = document.querySelectorAll('.buy-btn');

buyButtons.forEach(button => {
  button.addEventListener('click', () => {
    const name = button.dataset.name;
    const price = button.dataset.price;

    cart.push({ name, price });
    updateCartDrawer();
    openCart();
  });
});

const cartDrawer = document.createElement('div');
cartDrawer.classList.add('cart-drawer');
cartDrawer.innerHTML = `
  <div class="cart-header">
    <h3>Your Cart</h3>
    <button id="close-cart" class="icon-btn">×</button>
  </div>
  <div class="cart-items"></div>
  <div class="cart-footer">
    <div class="cart-total">Total: $0</div>
    <button id="checkout" class="btn primary">Checkout</button>
  </div>
`;

document.body.appendChild(cartDrawer);

const closeCartBtn = cartDrawer.querySelector('#close-cart');
const cartItemsDiv = cartDrawer.querySelector('.cart-items');
const cartTotalDiv = cartDrawer.querySelector('.cart-total');
const checkoutBtn = cartDrawer.querySelector('#checkout');

function updateCartDrawer() {
  cartItemsDiv.innerHTML = '';
  let total = 0;

  cart.forEach(item => {
    const div = document.createElement('div');
    div.textContent = `${item.name} - $${item.price}`;
    cartItemsDiv.appendChild(div);
    total += parseFloat(item.price);
  });

  cartTotalDiv.textContent = `Total: $${total}`;
}

function openCart() {
  cartDrawer.classList.add('open');
}

closeCartBtn.addEventListener('click', () => {
  cartDrawer.classList.remove('open');
});

// ===== Paystack Integration =====
checkoutBtn.addEventListener('click', () => {
  if(cart.length === 0) {
    alert("Your cart is empty!");
    return;
  }

  let total = cart.reduce((acc, item) => acc + parseFloat(item.price), 0) * 100; // convert to kobo
  let handler = PaystackPop.setup({
    key: 'pk_test_8974f63f00bdcbbc8258ea91930c3a8138bdf0b4', // your test key
    email: 'customer@example.com',
    amount: total,
    currency: "USD",
    ref: '' + Math.floor(Math.random() * 1000000000 + 1),
    callback: function(response){
      alert('Payment complete! Reference: ' + response.reference);
      cart = [];             // clear cart after payment
      updateCartDrawer();
      cartDrawer.classList.remove('open');
    },
    onClose: function(){
      alert('Transaction was not completed.');
    }
  });
  handler.openIframe();
});