<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/inc/_00meta.inc"%>
			
			<!-- 上端翻页 -->
			<ul class="pagination pagination-sm" >
				<c:if test="${pageData.hasPrev==true}">
					<li>
						<a
							href="javascript:js_post('psearch?searchMode=newest&searchKeywords=${pageData.searchKeywords}&page=${pageData.currentPageNo-1}')">&laquo;&nbsp;
						</a>
					</li>
				</c:if>
				<c:forEach items="${pageData.pageNoList}" var="obj">
					<c:if test="${obj=='...'}">
						<li class="disabled">
							<a href="#">${obj}</a>
						</li>
					</c:if>
					<c:if test="${obj.toString()==pageData.currentPageNo.toString()}">
						<li class="active">
							<a href="#">${obj} <span class="sr-only">(current)</span>
							</a>
						</li>
					</c:if>
					<c:if test="${obj!='...'&& obj!=pageData.currentPageNo}">
						<li>
							<a
								href="javascript:js_post('psearch?searchMode=${pageData.searchMode}&searchKeywords=${pageData.searchKeywords}&page=${obj}')">${obj}</a>
						</li>
					</c:if>
				</c:forEach>
				<c:if test="${pageData.hasNext==true}">
					<li>
						<a
							href="javascript:js_post('psearch?searchMode=${pageData.searchMode}&searchKeywords=${pageData.searchKeywords}&page=${pageData.currentPageNo+1}')">&nbsp;&raquo;</a>
					</li>
				</c:if>
			</ul>
			<!-- /上端翻页 -->