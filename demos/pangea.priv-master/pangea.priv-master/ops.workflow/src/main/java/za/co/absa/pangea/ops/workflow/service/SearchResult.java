package za.co.absa.pangea.ops.workflow.service;

import java.util.List;

public class SearchResult {

	private List<Response> data;
	private long total;
	private int size;
	private int start;
	
	
	public SearchResult(List<Response> data, long total, int start) {
		super();
		this.data = data;
		this.total = total;
		this.start = start;
		this.size = data.size();
	}
	
	public List<Response> getData() {
		return data;
	}
	public void setData(List<Response> data) {
		this.data = data;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	
}
