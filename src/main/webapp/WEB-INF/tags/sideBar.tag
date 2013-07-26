<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="recentTags" type="java.util.List" required="true"%>
<%@attribute name="relatedQuestions" type="java.util.List" required="false"%>

<aside class="sidebar">
	<tags:homeNewsList newses="${sidebarNews}" />
	<tags:recentTagsUsage tagsUsage="${recentTags}"/>
	<c:if test="${relatedQuestions != null }">
		<tags:relatedQuestions questions="${relatedQuestions}"/>
	</c:if>
</aside>