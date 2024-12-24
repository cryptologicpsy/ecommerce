package org.example.webapptest.model;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.Objects;

@Embeddable

public class CartItemId implements Serializable {

    private Long product_id;
    private String session_id;

    public CartItemId() {}

    public CartItemId(Long product_id, String session_id) {
        this.product_id = product_id;
        this.session_id = session_id;
    }

    // Getters και Setters


    public Long getProductId() {
        return product_id;
    }

    public void setProductId(Long product_id) {
        this.product_id = product_id;
    }

    public String getSessionId() {
        return session_id;
    }

    public void setSessionId(String session_id) {
        this.session_id = session_id;
    }

    // hashCode και equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItemId)) return false;
        CartItemId that = (CartItemId) o;
        return Objects.equals(getProductId(), that.getProductId()) &&
                Objects.equals(getSessionId(), that.getSessionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId(), getSessionId());
    }
}