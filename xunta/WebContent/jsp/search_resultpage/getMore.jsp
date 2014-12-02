
<c:forEach items="${getmore_DocsData}" var="docData">
	<li>
		<span class="date">${docData.date}</span>

			<div
				style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis; margin-right: 20%">
				<a href="${docData.url}" target="_blank"><font class="display_switch" style="display:none">(${docData.docID})</font>${docData.title}
				</a>
			</div>

		<p>
			${docData.getHighlightedContent()} <font class="display_switch" style="display:none">(${docData.score})</font>
		</p>
	</li>
</c:forEach>
