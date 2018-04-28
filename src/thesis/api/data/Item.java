/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis.api.data;

/**
 *
 * @author huynct
 */
public class Item {

    private long item_id;
    private String item_name;
    private String item_code = "";
    private String barcode = "";
    private long price;
    private long original_price = 0;
    private int order = 1;
    private int unit_id = 0;
    private int category_id = 0;
    private int cate_mask = 0;
    private int printer_mask = 0;
    private int kitchen_area_id = 0;
    private int inventory = 0;
    private long promotion_id = 0;
    private String img_path = "";
    private String img_crc = "";
    private String description = "";
    private int status;
    private String modified_by = "";
    private String modified_date_time = "";
    private int marked_delete = 0;

    public long getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(long original_price) {
        this.original_price = original_price;
    }
    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getCate_mask() {
        return cate_mask;
    }

    public void setCate_mask(int cate_mask) {
        this.cate_mask = cate_mask;
    }

    public int getPrinter_mask() {
        return printer_mask;
    }

    public void setPrinter_mask(int printer_mask) {
        this.printer_mask = printer_mask;
    }

    public int getKitchen_area_id() {
        return kitchen_area_id;
    }

    public void setKitchen_area_id(int kitchen_area_id) {
        this.kitchen_area_id = kitchen_area_id;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public long getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(long promotion_id) {
        this.promotion_id = promotion_id;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getImg_crc() {
        return img_crc;
    }

    public void setImg_crc(String img_crc) {
        this.img_crc = img_crc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public String getModified_date_time() {
        return modified_date_time;
    }

    public void setModified_date_time(String modified_date_time) {
        this.modified_date_time = modified_date_time;
    }

    public int getMarked_delete() {
        return marked_delete;
    }

    public void setMarked_delete(int marked_delete) {
        this.marked_delete = marked_delete;
    }


    /*huynct add kitchen_area_name + printer_mask*/
}
