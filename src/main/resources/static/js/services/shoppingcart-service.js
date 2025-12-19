let cartService;

class ShoppingCartService {

    cart = {
        items:[],
        total:0
    };
    
    selectedItems = new Set();

    addToCart(productId)
    {
        const url = `${config.baseUrl}/cart/products/${productId}`;
        // const headers = userService.getHeaders();

        axios.post(url, {})// ,{headers})
            .then(response => {
                this.setCart(response.data)

                this.updateCartDisplay()

            })
            .catch(error => {

                const data = {
                    error: "Add to cart failed."
                };

                templateBuilder.append("error", data, "errors")
            })
    }

    setCart(data)
    {
        this.cart = {
            items: [],
            total: 0
        }

        this.cart.total = data.total;

        for (const [key, value] of Object.entries(data.items)) {
            this.cart.items.push(value);
        }
    }

    loadCart()
    {

        const url = `${config.baseUrl}/cart`;

        axios.get(url)
            .then(response => {
                this.setCart(response.data)

                this.updateCartDisplay()

            })
            .catch(error => {

                const data = {
                    error: "Load cart failed."
                };

                templateBuilder.append("error", data, "errors")
            })

    }

    loadCartPage()
    {
        // templateBuilder.build("cart", this.cart, "main");

        const main = document.getElementById("main")
        main.innerHTML = "";

        let div = document.createElement("div");
        div.classList="filter-box";
        main.appendChild(div);

        const contentDiv = document.createElement("div")
        contentDiv.id = "content";
        contentDiv.classList.add("content-form");

        const cartHeader = document.createElement("div")
        cartHeader.classList.add("cart-header")

        const h1 = document.createElement("h1")
        h1.innerText = "Shopping Cart";
        cartHeader.appendChild(h1);

        const buttonGroup = document.createElement("div");
        buttonGroup.style.display = "flex";
        buttonGroup.style.gap = "10px";

        const selectAllCheckbox = document.createElement("input");
        selectAllCheckbox.type = "checkbox";
        selectAllCheckbox.id = "select-all";
        selectAllCheckbox.addEventListener("change", (e) => this.toggleSelectAll(e.target.checked));
        
        const selectAllLabel = document.createElement("label");
        selectAllLabel.htmlFor = "select-all";
        selectAllLabel.innerText = "Select All";
        selectAllLabel.style.marginRight = "15px";
        selectAllLabel.style.display = "flex";
        selectAllLabel.style.alignItems = "center";
        selectAllLabel.style.gap = "5px";
        selectAllLabel.prepend(selectAllCheckbox);
        buttonGroup.appendChild(selectAllLabel);

        const clearButton = document.createElement("button");
        clearButton.classList.add("btn", "btn-danger");
        clearButton.innerText = "Clear All";
        clearButton.addEventListener("click", () => this.clearCart());
        buttonGroup.appendChild(clearButton);

        const proceedButton = document.createElement("button");
        proceedButton.classList.add("btn", "btn-success");
        proceedButton.id = "proceed-checkout-btn";
        proceedButton.innerText = "Proceed to Checkout";
        proceedButton.addEventListener("click", () => this.showCheckoutPage());
        buttonGroup.appendChild(proceedButton);

        cartHeader.appendChild(buttonGroup);
        contentDiv.appendChild(cartHeader)
        main.appendChild(contentDiv);

        // let parent = document.getElementById("cart-item-list");
        this.cart.items.forEach(item => {
            this.buildItem(item, contentDiv)
        });

        // Add total display
        if (this.cart.items.length > 0)
    buildItem(item, parent)
    {
        let outerDiv = document.createElement("div");
        outerDiv.classList.add("cart-item");
        outerDiv.style.display = "flex";
        outerDiv.style.gap = "15px";
        outerDiv.style.alignItems = "center";

        // Checkbox for selection
        let checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.classList.add("item-checkbox");
        checkbox.dataset.productId = item.product.productId;
        checkbox.checked = this.selectedItems.has(item.product.productId);
        checkbox.addEventListener("change", (e) => this.toggleItemSelection(item.product.productId, e.target.checked));
        outerDiv.appendChild(checkbox);

        let div = document.createElement("div");
        outerDiv.appendChild(div);
        let h4 = document.createElement("h4")
        h4.innerText = item.product.name;
        div.appendChild(h4);
            const totalH3 = document.createElement("h3");
            totalH3.innerText = `Total: $${this.cart.total.toFixed(2)}`;
            totalDiv.appendChild(totalH3);
            
            contentDiv.appendChild(totalDiv);
        }
    }

    buildItem(item, parent)
    {
        let outerDiv = document.createElement("div");
        outerDiv.classList.add("cart-item");

        let div = document.createElement("div");
        outerDiv.appendChild(div);
        let h4 = document.createElement("h4")
        h4.innerText = item.product.name;
        div.appendChild(h4);

        let photoDiv = document.createElement("div");
        photoDiv.classList.add("photo")
        let img = document.createElement("img");
        img.src = `${config.baseUrl}/images/products/${item.product.imageUrl}`
        img.addEventListener("click", () => {
            showImageDetailForm(item.product.name, img.src)
        })
        photoDiv.appendChild(img)
        let priceH4 = document.createElement("h4");
        priceH4.classList.add("price");
        priceH4.innerText = `$${item.product.price}`;
        photoDiv.appendChild(priceH4);
        outerDiv.appendChild(photoDiv);

        let descriptionDiv = document.createElement("div");
        descriptionDiv.innerText = item.product.description;
        outerDiv.appendChild(descriptionDiv);

        let quantityDiv = document.createElement("div")
        quantityDiv.innerText = `Quantity: ${item.quantity}`;
        outerDiv.appendChild(quantityDiv)


        parent.appendChild(outerDiv);
    }

    clearCart()
    {

        const url = `${config.baseUrl}/cart`;

        axios.delete(url)
             .then(response => {
                 this.cart = {
                     items: [],
                     total: 0
                 }

                 this.cart.total = response.data.total;

                 for (const [key, value] of Object.entries(response.data.items)) {
                     this.cart.items.push(value);
                 }

                 this.updateCartDisplay()
                 this.loadCartPage()

             })
             .catch(error => {

                 const data = {
                     error: "Empty cart failed."
                 };

                 templateBuilder.append("error", data, "errors")
             })
    }

    updateCartDisplay()
    {
        try {
            const itemCount = this.cart.items.length;
            const cartControl = document.getElementById("cart-items")

            cartControl.innerText = itemCount;
        }
        catch (e) {

        }
    }

    toggleItemSelection(productId, isSelected)
    {
        if (isSelected) {
            this.selectedItems.add(productId);
        } else {
            this.selectedItems.delete(productId);
        }
    }

    toggleSelectAll(selectAll)
    {
        this.selectedItems.clear();
        
        if (selectAll) {
            this.cart.items.forEach(item => {
                this.selectedItems.add(item.product.productId);
            });
        }
        
        // Update all checkboxes
        document.querySelectorAll('.item-checkbox').forEach(checkbox => {
            checkbox.checked = selectAll;
        });
    }

    showCheckoutPage()
    {
        if (this.selectedItems.size === 0)
        {
            const data = {
                error: "Please select at least one item to checkout."
            };
            templateBuilder.append("error", data, "errors");
            return;
        }

        const main = document.getElementById("main");
        main.innerHTML = "";

        const contentDiv = document.createElement("div");
        contentDiv.id = "content";
        contentDiv.classList.add("content-form");

        const checkoutHeader = document.createElement("div");
        checkoutHeader.classList.add("cart-header");

        const h1 = document.createElement("h1");
        h1.innerText = "Checkout";
        checkoutHeader.appendChild(h1);

        contentDiv.appendChild(checkoutHeader);

        const checkoutInfo = document.createElement("div");
        checkoutInfo.style.marginTop = "20px";
        checkoutInfo.style.marginBottom = "20px";

        const infoText = document.createElement("p");
        infoText.innerText = "Review your selected items:";
        infoText.style.fontSize = "18px";
        infoText.style.fontWeight = "bold";
        checkoutInfo.appendChild(infoText);

        contentDiv.appendChild(checkoutInfo);

        // Display selected items
        let checkoutTotal = 0;
        this.cart.items.forEach(item => {
            if (this.selectedItems.has(item.product.productId)) {
                this.buildCheckoutItem(item, contentDiv);
                checkoutTotal += item.lineTotal;
            }
        });

        // Display total
        const totalDiv = document.createElement("div");
        totalDiv.classList.add("cart-total");
        totalDiv.style.marginTop = "20px";
        totalDiv.style.padding = "15px";
        totalDiv.style.backgroundColor = "#f8f9fa";
        totalDiv.style.border = "1px solid #dee2e6";
        totalDiv.style.borderRadius = "5px";
        totalDiv.style.textAlign = "right";

        const totalH3 = document.createElement("h3");
        totalH3.innerText = `Total: $${checkoutTotal.toFixed(2)}`;
        totalDiv.appendChild(totalH3);

        contentDiv.appendChild(totalDiv);

        // Buttons
        const buttonDiv = document.createElement("div");
        buttonDiv.style.marginTop = "20px";
        buttonDiv.style.display = "flex";
        buttonDiv.style.gap = "10px";
        buttonDiv.style.justifyContent = "flex-end";

        const backButton = document.createElement("button");
        backButton.classList.add("btn", "btn-secondary");
        backButton.innerText = "Back to Cart";
        backButton.addEventListener("click", () => this.loadCartPage());
        buttonDiv.appendChild(backButton);

        const confirmButton = document.createElement("button");
        confirmButton.classList.add("btn", "btn-success");
        confirmButton.innerText = "Confirm Order";
        confirmButton.addEventListener("click", () => this.confirmCheckout());
        buttonDiv.appendChild(confirmButton);

        contentDiv.appendChild(buttonDiv);
        main.appendChild(contentDiv);
    }

    buildCheckoutItem(item, parent)
    {
        let outerDiv = document.createElement("div");
        outerDiv.classList.add("cart-item");
        outerDiv.style.display = "flex";
        outerDiv.style.gap = "15px";
        outerDiv.style.alignItems = "center";
        outerDiv.style.backgroundColor = "#f8f9fa";
        outerDiv.style.padding = "10px";
        outerDiv.style.marginBottom = "10px";
        outerDiv.style.borderRadius = "5px";

        let div = document.createElement("div");
        div.style.flex = "1";
        
        let h4 = document.createElement("h4");
        h4.innerText = item.product.name;
        div.appendChild(h4);

        let priceDiv = document.createElement("div");
        priceDiv.innerText = `Price: $${item.product.price} x ${item.quantity}`;
        div.appendChild(priceDiv);

        let lineTotalDiv = document.createElement("div");
        lineTotalDiv.innerText = `Subtotal: $${item.lineTotal.toFixed(2)}`;
        lineTotalDiv.style.fontWeight = "bold";
        div.appendChild(lineTotalDiv);

        outerDiv.appendChild(div);
        parent.appendChild(outerDiv);
    }

    async confirmCheckout()
    {
        if (this.selectedItems.size === 0)
        {
            const data = {
                error: "No items selected for checkout."
            };
            templateBuilder.append("error", data, "errors");
            return;
        }

        try {
            // Get selected product IDs
            const selectedProductIds = Array.from(this.selectedItems);

            // First, remove unselected items temporarily (we'll add them back after checkout)
            const itemsToCheckout = this.cart.items.filter(item => 
                this.selectedItems.has(item.product.productId)
            );

            // Call checkout endpoint
            const url = `${config.baseUrl}/orders`;
            const response = await axios.post(url, {});

            // Remove checked out items from local cart
            this.cart.items = this.cart.items.filter(item => 
                !this.selectedItems.has(item.product.productId)
            );

            // Delete checked out items from backend cart
            for (let productId of selectedProductIds) {
                const deleteUrl = `${config.baseUrl}/cart/products/${productId}`;
                try {
                    await axios.delete(deleteUrl);
                } catch (err) {
                    console.error(`Failed to delete product ${productId}:`, err);
                }
            }

            // Recalculate total
            this.cart.total = this.cart.items.reduce((sum, item) => sum + item.lineTotal, 0);

            // Clear selected items
            this.selectedItems.clear();

            // Update cart display
            this.updateCartDisplay();

            // Show success message
            const data = {
                message: `Order #${response.data.orderId} has been placed successfully! Thank you for your purchase.`
            };

            templateBuilder.append("message", data, "main");

            // Redirect to home after delay
            setTimeout(() => {
                window.location.hash = '#home';
            }, 3000);

        } catch (error) {
            let errorMessage = "Checkout failed.";
            
            if (error.response && error.response.data) {
                errorMessage = error.response.data.message || errorMessage;
            }

            const data = {
                error: errorMessage
            };

            templateBuilder.append("error", data, "errors");
        }
    }
}





document.addEventListener('DOMContentLoaded', () => {
    cartService = new ShoppingCartService();

    if(userService.isLoggedIn())
    {
        cartService.loadCart();
    }

});
