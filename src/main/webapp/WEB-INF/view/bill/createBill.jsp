<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ include file="../shared/layout.jsp"%>
<!-- Main Content -->
<div class="px-8 py-6">
	<!-- Page Header -->
	<div class="flex items-center justify-between mb-8">
		<div>
			<h1 class="text-3xl font-bold text-gray-900">Create New Invoice</h1>
			<p class="text-gray-600 mt-2">Generate a professional invoice for your customer</p>
		</div>
		<a href="bill?action=list"
			class="bg-gray-100 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-200 transition-colors duration-200 flex items-center">
			<i class="fas fa-arrow-left mr-2"></i>Back to Invoices
		</a>
	</div>

	<!-- Alerts -->
	<c:if test="${not empty success}">
		<div class="mb-6 bg-emerald-50 border border-emerald-200 rounded-xl p-4">
			<div class="flex items-center">
				<i class="fas fa-check-circle text-green-500 mr-2"></i> <span
					class="text-green-800">${success}</span>
			</div>
		</div>
	</c:if>

	<c:if test="${not empty error}">
		<div class="mb-6 bg-red-50 border border-red-200 rounded-xl p-4">
			<div class="flex items-center">
				<i class="fas fa-exclamation-circle text-red-500 mr-2"></i> <span
					class="text-red-800">${error}</span>
			</div>
		</div>
	</c:if>

	<!-- Invoice Form -->
	<div class="bg-white rounded-xl shadow-lg border border-gray-200 overflow-hidden">
		<!-- Form Header -->
		<div class="bg-gradient-to-r from-orange-500 to-orange-600 px-6 py-4">
			<h2 class="text-xl font-semibold text-white flex items-center">
				<i class="fas fa-file-invoice mr-3"></i>Invoice Details
			</h2>
		</div>

		<form action="bill" method="post" class="p-6 space-y-8" onsubmit="return handleFormSubmission(event)">
			<input type="hidden" name="action" value="create">

			<!-- Customer Information Section -->
			<div class="bg-gray-50 rounded-lg p-6">
				<h3 class="text-lg font-semibold text-gray-900 mb-4 flex items-center">
					<i class="fas fa-user-circle mr-2 text-cyan-600"></i>Customer Information
				</h3>
				<div class="grid grid-cols-1 md:grid-cols-2 gap-6">
					<div>
						<label for="customerId" class="block text-sm font-medium text-gray-700 mb-2">
							Select Customer <span class="text-gray-400">(Optional - Leave empty for walk-in customer)</span>
						</label>
						<select name="customerId" id="customerId"
							class="w-full px-4 py-3 border ${fieldErrors.customerId != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-cyan-500 focus:border-cyan-500'} rounded-lg transition-colors duration-200 bg-white">
							<option value="">Choose a customer or leave empty for walk-in...</option>
							<c:forEach var="customer" items="${customers}">
								<option value="${customer.customerId}" ${customerId == customer.customerId ? 'selected' : ''}>
									${customer.name} (${customer.accountNumber})
								</option>
							</c:forEach>
						</select>
						<c:if test="${fieldErrors.customerId != null}">
							<p class="mt-1 text-sm text-red-600">${fieldErrors.customerId}</p>
						</c:if>
					</div>
				</div>
			</div>

			<!-- Invoice Items Section -->
			<div class="bg-white border border-gray-200 rounded-lg overflow-hidden">
				<div class="bg-gray-50 px-6 py-4 border-b border-gray-200">
					<h3 class="text-lg font-semibold text-gray-900 flex items-center">
						<i class="fas fa-shopping-cart mr-2 text-cyan-600"></i>Invoice Items
					</h3>
					<p class="text-sm text-gray-600 mt-1">Add products or services to this invoice</p>
				</div>
				
				<div class="p-6">
					<!-- Items Table -->
					<div class="overflow-x-auto">
						<table class="min-w-full divide-y divide-gray-200">
							<thead class="bg-gray-50/80">
								<tr>
									<th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
										Product/Service
									</th>
									<th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
										Quantity
									</th>
									<th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
										Unit Price
									</th>
									<th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
										Total
									</th>
									<th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
										Action
									</th>
								</tr>
							</thead>
							<tbody id="items-container" class="bg-white divide-y divide-gray-200">
								<tr class="item-row">
									<td class="px-4 py-4">
										<select name="productId" required
											class="w-full px-3 py-2 border ${fieldErrors.productId != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-cyan-500 focus:border-cyan-500'} rounded-lg transition-colors duration-200">
											<option value="">Select Product...</option>
											<c:forEach var="product" items="${products}">
												<option value="${product.productId}" data-price="${product.price}" data-stock="${product.quantity}">
													${product.name} - Rs. ${product.price} (Stock: ${product.quantity})
												</option>
											</c:forEach>
										</select>
										<c:if test="${fieldErrors.productId != null}">
											<p class="mt-1 text-sm text-red-600">${fieldErrors.productId}</p>
										</c:if>
									</td>
									<td class="px-4 py-4">
										<input type="number" name="quantity" min="1" value="1" required
											class="w-full px-3 py-2 border ${fieldErrors.quantity != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-cyan-500 focus:border-cyan-500'} rounded-lg transition-colors duration-200"
											placeholder="Qty">
										<c:if test="${fieldErrors.quantity != null}">
											<p class="mt-1 text-sm text-red-600">${fieldErrors.quantity}</p>
										</c:if>
										<!-- Stock validation error display -->
										<c:forEach var="error" items="${fieldErrors}">
											<c:if test="${error.key.startsWith('stock_')}">
												<p class="mt-1 text-sm text-red-600">${error.value}</p>
											</c:if>
										</c:forEach>
									</td>
									<td class="px-4 py-4">
										<input type="text" class="unit-price-display w-full px-3 py-2 border border-gray-300 rounded-lg bg-gray-50" placeholder="Unit Price" readonly>
										<input type="hidden" name="unitPrice" class="unit-price-hidden">
									</td>
									<td class="px-4 py-4">
										<input type="text" class="subtotal w-full px-3 py-2 border border-gray-300 rounded-lg bg-gray-50" placeholder="Subtotal" readonly>
									</td>
									<td class="px-4 py-4">
										<button type="button" class="remove-btn text-red-600 hover:text-red-800 transition-colors duration-200" onclick="removeItem(this)">
											<i class="fas fa-trash"></i>
										</button>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					
					<div class="mt-4">
						<button type="button" class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors duration-200 flex items-center" onclick="addItem()">
							<i class="fas fa-plus mr-2"></i>Add Item
						</button>
					</div>
				</div>
			</div>

			<!-- Invoice Summary Section -->
			<div class="bg-gray-50 rounded-lg p-6">
				<h3 class="text-lg font-semibold text-gray-900 mb-4 flex items-center">
					<i class="fas fa-calculator mr-2 text-cyan-600"></i>Invoice Summary
				</h3>
				
				<div class="grid grid-cols-1 md:grid-cols-2 gap-6">
					<!-- Bill Discount -->
					<div>
						<label for="billDiscount" class="block text-sm font-medium text-gray-700 mb-2">
							Bill Discount (%)
						</label>
						<input type="number" name="billDiscount" id="billDiscount" min="0" max="100" value="0" step="0.01"
							class="w-full px-4 py-3 border ${fieldErrors.billDiscount != null ? 'border-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-cyan-500 focus:border-cyan-500'} rounded-lg transition-colors duration-200"
							placeholder="0.00" onchange="calculateBillTotal()">
						<c:if test="${fieldErrors.billDiscount != null}">
							<p class="mt-1 text-sm text-red-600">${fieldErrors.billDiscount}</p>
						</c:if>
						<p class="text-xs text-gray-500 mt-1">Enter discount percentage for the entire invoice</p>
					</div>
					
					<!-- Total Amount -->
					<div class="bg-white rounded-lg p-4 border border-gray-200">
						<div class="flex justify-between items-center mb-2">
							<span class="text-sm text-gray-600">Subtotal:</span>
							<span class="text-sm text-gray-900" id="subtotal-amount">Rs. 0.00</span>
						</div>
						<div class="flex justify-between items-center mb-2">
							<span class="text-sm text-gray-600">Discount:</span>
							<span class="text-sm text-green-600" id="discount-amount">Rs. 0.00</span>
						</div>
						<hr class="my-2">
						<div class="flex justify-between items-center">
							<span class="text-lg font-semibold text-gray-900">Total Amount:</span>
							<span class="text-xl font-bold text-cyan-600" id="total-amount">Rs. 0.00</span>
						</div>
					</div>
				</div>
			</div>

			<!-- Submit Section -->
			<div class="flex justify-center pt-6">
				<button type="submit" class="bg-gradient-to-r from-cyan-500 to-blue-600 text-white px-8 py-3 rounded-lg hover:from-cyan-600 hover:to-blue-700 transition-colors duration-200 flex items-center text-lg font-semibold">
					<i class="fas fa-file-invoice-dollar mr-2"></i>Generate Invoice
				</button>
			</div>
		</form>
	</div>
</div>

<script>
    function handleFormSubmission(event) {
        const customerId = document.getElementById('customerId').value;
        
        // Check if no customer is selected
        if (!customerId || customerId.trim() === '') {
            // Show confirmation dialog for walk-in customer
            const confirmed = confirm(
                "No customer selected. Do you want to proceed with a walk-in customer?\n\n" +
                "Click 'OK' to create bill for walk-in customer or 'Cancel' to select a customer."
            );
            
            if (!confirmed) {
                event.preventDefault();
                return false;
            }
        }
        
        return true;
    }

    function addItem() {
        const container = document.getElementById('items-container');
        const row = container.children[0].cloneNode(true);
        
        // Clear values
        row.querySelector('select').value = '';
        row.querySelector('input[name="quantity"]').value = '1';
        row.querySelector('.unit-price-display').value = '';
        row.querySelector('.unit-price-hidden').value = '';
        row.querySelector('.subtotal').value = '';
        
        addEventListeners(row);
        container.appendChild(row);
    }

    function removeItem(btn) {
        const container = document.getElementById('items-container');
        if (container.children.length > 1) {
            btn.closest('.item-row').remove();
            calculateBillTotal();
        }
    }

    function addEventListeners(row) {
        const select = row.querySelector('select');
        const quantity = row.querySelector('input[name="quantity"]');
        const unitPriceDisplay = row.querySelector('.unit-price-display');
        const unitPriceHidden = row.querySelector('.unit-price-hidden');
        const subtotal = row.querySelector('.subtotal');

        select.addEventListener('change', function () {
            const option = this.options[this.selectedIndex];
            const price = option.getAttribute('data-price');
            const stock = option.getAttribute('data-stock');
            if (price) {
                unitPriceDisplay.value = 'Rs. ' + price;
                unitPriceHidden.value = price;
                
                // Update quantity max attribute based on stock
                if (stock) {
                    quantity.setAttribute('max', stock);
                    if (parseInt(quantity.value) > parseInt(stock)) {
                        quantity.value = stock;
                    }
                }
            } else {
                unitPriceDisplay.value = '';
                unitPriceHidden.value = '';
                quantity.removeAttribute('max');
            }
            calculateSubtotal(row);
        });

        quantity.addEventListener('input', function () {
            const option = select.options[select.selectedIndex];
            const stock = option.getAttribute('data-stock');
            
            if (stock && parseInt(this.value) > parseInt(stock)) {
                this.value = stock;
                alert('Quantity cannot exceed available stock (' + stock + ')');
            }
            calculateSubtotal(row);
        });
    }

    function calculateSubtotal(row) {
        const select = row.querySelector('select');
        const quantity = row.querySelector('input[name="quantity"]');
        const unitPriceHidden = row.querySelector('.unit-price-hidden');
        const subtotal = row.querySelector('.subtotal');

        const option = select.options[select.selectedIndex];
        const price = option.getAttribute('data-price');
        const qty = quantity.value;

        if (price && qty) {
            const value = parseFloat(price) * parseInt(qty);
            subtotal.value = 'Rs. ' + value.toFixed(2);
        } else {
            subtotal.value = '';
        }
        calculateBillTotal();
    }

    function calculateBillTotal() {
        const subtotals = document.querySelectorAll('.subtotal');
        let subtotalSum = 0;
        
        subtotals.forEach(subtotal => {
            const value = subtotal.value.replace('Rs. ', '');
            if (value) {
                subtotalSum += parseFloat(value);
            }
        });

        const discountPercent = parseFloat(document.getElementById('billDiscount').value) || 0;
        const discountAmount = subtotalSum * (discountPercent / 100);
        const totalAmount = subtotalSum - discountAmount;

        document.getElementById('subtotal-amount').textContent = 'Rs. ' + subtotalSum.toFixed(2);
        document.getElementById('discount-amount').textContent = 'Rs. ' + discountAmount.toFixed(2);
        document.getElementById('total-amount').textContent = 'Rs. ' + totalAmount.toFixed(2);
    }

    // Initialize event listeners for the first row
    document.addEventListener('DOMContentLoaded', function() {
        const firstRow = document.querySelector('.item-row');
        if (firstRow) {
            addEventListeners(firstRow);
        }
        
        // Add event listener for bill discount
        document.getElementById('billDiscount').addEventListener('input', calculateBillTotal);
    });
</script>
<footer class="border-t border-gray-200 bg-white mt-auto px-8 py-4">
    <p class="text-center text-xs text-gray-400">&copy; 2026 Ocean View Resort, Galle, Sri Lanka. All rights reserved.</p>
</footer>
    </div>
</div>
</body>
</html>