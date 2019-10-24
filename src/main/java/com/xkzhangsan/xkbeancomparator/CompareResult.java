package com.xkzhangsan.xkbeancomparator;

public class CompareResult {
	
	private boolean isChanged;
	
	private String changeContent;

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	public String getChangeContent() {
		return changeContent;
	}

	public void setChangeContent(String changeContent) {
		this.changeContent = changeContent;
	}

}
