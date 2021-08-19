package com.fpoly.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bill_detail")
public class Bill_Detail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer IDBillDetail;
	
	@Column(name = "BillDetailStatus")
	private boolean billDetailStatus;
	
	@Column(name = "Note")
	private String note;
	
	@Column(name = "Note2")
	private String note2;
	
	@Column(name = "qty")
	private int qty;
	
	@Column(name = "size")
	private String size;
	
	@Column(name = "price")
	private Integer price;
	
	@Column(name = "color")
	private String color;
	
	@Column(name = "CreateDate")
	private Date createDate;
	
	@ManyToOne
	@JoinColumn(name = "Product_Detail_id")
	private Product_Detail productDetail;
	
	@ManyToOne
	@JoinColumn(name = "Bill_id")
	private Bill bill;
	
	public boolean canUpdateQty(Integer qty) {
		return qty == null || qty <= 0 || this.getProductDetail().hasStock(qty);
	}
	
	public BigDecimal getSubtotal() {
		return new BigDecimal(productDetail.getPrice()).multiply(new BigDecimal(qty));
	}

	public void addQuantity(int qty) {
		if (qty > 0) {
			this.qty = this.qty + qty;
		}
	}
	
	public boolean hasSameSizeThan(String size2) {
		return this.size.equals(size2);
	}
	
	public Bill_Detail() {
		
	}

	public Integer getIDBillDetail() {
		return IDBillDetail;
	}

	public void setIDBillDetail(Integer iDBillDetail) {
		IDBillDetail = iDBillDetail;
	}

	public boolean isBillDetailStatus() {
		return billDetailStatus;
	}

	public void setBillDetailStatus(boolean billDetailStatus) {
		this.billDetailStatus = billDetailStatus;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNote2() {
		return note2;
	}

	public void setNote2(String note2) {
		this.note2 = note2;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Product_Detail getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(Product_Detail productDetail) {
		this.productDetail = productDetail;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}
	
	
}
