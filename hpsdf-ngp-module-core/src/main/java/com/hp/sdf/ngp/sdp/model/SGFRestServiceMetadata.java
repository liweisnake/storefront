/*
 * Copyright (c) 2009 Hewlett-Packard Company, All Rights Reserved.
 *
 * RESTRICTED RIGHTS LEGEND Use, duplication, or disclosure by the U.S.
 * Government is subject to restrictions as set forth in sub-paragraph
 * (c)(1)(ii) of the Rights in Technical Data and Computer Software
 * clause in DFARS 252.227-7013.
 *
 * Hewlett-Packard Company
 * 3000 Hanover Street
 * Palo Alto, CA 94304 U.S.A.
 * Rights for non-DOD U.S. Government Departments and Agencies are as
 * set forth in FAR 52.227-19(c)(1,2).
 */
package com.hp.sdf.ngp.sdp.model;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

public class SGFRestServiceMetadata implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String serviceId;
	
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public HashSet<SampleRequestGet> getSampleRequestGet() {
		return sampleRequestGet;
	}

	public void setSampleRequestGet(HashSet<SampleRequestGet> sampleRequestGet) {
		this.sampleRequestGet = sampleRequestGet;
	}

	public HashSet<SampleRequestPost> getSampleRequestPost() {
		return sampleRequestPost;
	}

	public void setSampleRequestPost(HashSet<SampleRequestPost> sampleRequestPost) {
		this.sampleRequestPost = sampleRequestPost;
	}

	public HashMap<String, SampleResponse> getSampleResponse() {
		return sampleResponse;
	}

	public void setSampleResponse(HashMap<String, SampleResponse> sampleResponse) {
		this.sampleResponse = sampleResponse;
	}

	private HashSet<SampleRequestGet> sampleRequestGet;
	
	private HashSet<SampleRequestPost> sampleRequestPost;
	
	private HashMap<String, SampleResponse> sampleResponse;
	
	
	
	public class SampleRequestGet {
		
		private String contentType;
		
		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public String getMetadata() {
			return metadata;
		}

		public void setMetadata(String metadata) {
			this.metadata = metadata;
		}

		private String metadata;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((contentType == null) ? 0 : contentType.hashCode());
			result = prime * result
					+ ((metadata == null) ? 0 : metadata.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SampleRequestGet other = (SampleRequestGet) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (contentType == null) {
				if (other.contentType != null)
					return false;
			} else if (!contentType.equals(other.contentType))
				return false;
			if (metadata == null) {
				if (other.metadata != null)
					return false;
			} else if (!metadata.equals(other.metadata))
				return false;
			return true;
		}

		private SGFRestServiceMetadata getOuterType() {
			return SGFRestServiceMetadata.this;
		}
		
		
		
	}
	
	public class SampleRequestPost implements Comparator<SampleRequestPost>{
		
		private String contentType;
		
		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public String getMetadata() {
			return metadata;
		}

		public void setMetadata(String metadata) {
			this.metadata = metadata;
		}

		private String metadata;

		public int compare(SampleRequestPost o1, SampleRequestPost o2) {
			return Collator.getInstance().compare(o1.getContentType(), o2.getContentType());
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((contentType == null) ? 0 : contentType.hashCode());
			result = prime * result
					+ ((metadata == null) ? 0 : metadata.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SampleRequestPost other = (SampleRequestPost) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (contentType == null) {
				if (other.contentType != null)
					return false;
			} else if (!contentType.equals(other.contentType))
				return false;
			if (metadata == null) {
				if (other.metadata != null)
					return false;
			} else if (!metadata.equals(other.metadata))
				return false;
			return true;
		}

		private SGFRestServiceMetadata getOuterType() {
			return SGFRestServiceMetadata.this;
		}
		
	}
	
	public class SampleResponse implements Comparator<SampleResponse>{
		
		private String contentType;
		
		private HashSet<ResponseMeta> responseMeta;
		
		public HashSet<ResponseMeta> getResponseMeta() {
			return responseMeta;
		}

		public void setResponseMeta(HashSet<ResponseMeta> responseMeta) {
			this.responseMeta = responseMeta;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public int compare(SampleResponse o1, SampleResponse o2) {
			return Collator.getInstance().compare(o1.getContentType(), o2.getContentType());
		}
		
		
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((contentType == null) ? 0 : contentType.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SampleResponse other = (SampleResponse) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (contentType == null) {
				if (other.contentType != null)
					return false;
			} else if (!contentType.equals(other.contentType))
				return false;
			return true;
		}



		public class ResponseMeta implements Comparator<ResponseMeta>{
			
			private String status;
			
			private String metadata;

			public String getStatus() {
				return status;
			}

			public void setStatus(String status) {
				this.status = status;
			}

			public String getMetadata() {
				return metadata;
			}

			public void setMetadata(String metadata) {
				this.metadata = metadata;
			}

			public int compare(ResponseMeta o1, ResponseMeta o2) {
				return Collator.getInstance().compare(o1.status, o2.status);
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + getOuterType().hashCode();
				result = prime * result
						+ ((metadata == null) ? 0 : metadata.hashCode());
				result = prime * result
						+ ((status == null) ? 0 : status.hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				ResponseMeta other = (ResponseMeta) obj;
				if (!getOuterType().equals(other.getOuterType()))
					return false;
				if (metadata == null) {
					if (other.metadata != null)
						return false;
				} else if (!metadata.equals(other.metadata))
					return false;
				if (status == null) {
					if (other.status != null)
						return false;
				} else if (!status.equals(other.status))
					return false;
				return true;
			}

			private SampleResponse getOuterType() {
				return SampleResponse.this;
			}
			
		}

		private SGFRestServiceMetadata getOuterType() {
			return SGFRestServiceMetadata.this;
		}
		
	}

}

// $Id$