package com.joakimsvedin.whatsfordinner;

public class Ingredient {

    private String name;
    private String unit;
    private float quantity;

    public Ingredient(String name, float quantity, String unit) {
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public String getUnit() {
        return unit.toLowerCase();
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public Ingredient getClone() {
        Ingredient ingredient = new Ingredient(this.name, this.quantity, this.unit);
        return ingredient;
    }

    public void increaseQuantity(int dx){
        quantity += dx;
    }

}
