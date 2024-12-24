package org.example.webapptest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Photo {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String imagePath;

	    // Getters and setters
	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getImagePath() {
	        return imagePath;
	    }

	    public void setImagePath(String imagePath) {
	        this.imagePath = imagePath;
	    }

}
