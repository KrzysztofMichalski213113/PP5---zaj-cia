package pl.kkanclerz.sales;

public class SalesFacade {
    InMemoryCartStorage cartStorage;
    private ProductDetailsProvider productDetailsProvider;

    public SalesFacade(InMemoryCartStorage cartStorage, ProductDetailsProvider productDetailsProvider) {
        this.cartStorage = cartStorage;
        this.productDetailsProvider = productDetailsProvider;
    }

    public void addToCart(String customerId, String productId) {
        Cart cart = cartStorage.loadForCustomer(customerId)
                .orElse(Cart.empty());

        Product product = productDetailsProvider.getDetails(productId);

        cart.addProduct(product);

        cartStorage.save(customerId, cart);
    }
}