package com.josdem.vetlog.record;

import jakarta.validation.constraints.Size;

public record AdoptionRecord (
	String uuid,
    @Size(min = 1, max = 1000)
    String description)
 implements IRecord {
	
	public static Builder builder() {
        return new Builder();
    }
	
	 public static class Builder {
	        private String uuid;
	        private String description;
	        
	        public Builder uuid(String uuid) {
	            this.uuid = uuid;
	            return this;
	        }
	        
	        public Builder description(String description) {
	            this.description = description;
	            return this;
	        }
	        
	        public AdoptionRecord build() {
	            
	            return new AdoptionRecord(
	            		uuid,
	            		description
	                    );
	        }
	    }
	 }


