#{if false} Arguments: _question, _editable #{/if}
<div class="tags">
	#{form @Secured.updateTags(_question.id())}
	#{list items:_question.getTags(), as:'tag'}
		<a href="@{Search.searchQuestion(tag.getName())}" class="tag">${tag.getName()}</a>
	#{/list}
	#{if _editable}
		<input type="button" class="editTags" value="Edit Tags" tagsJSON="@{Application.tags}">
	#{/if}
	#{/form}
</div>
