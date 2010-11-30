#{if false} Arguments: _answer, _user, _extended #{/if}
<li class="question #{if _user && _answer.getQuestion().owner() == _user}own#{/if}"
	onclick="goto('@{Application.question(_answer.getQuestion().id())}')">
	<h2 id="question-${_answer.getQuestion().id()}">#{showProfile _answer.getQuestion() /}:</h2>
	<p>${_answer.getQuestion().summary()}</p>
	#{date _answer.getQuestion() /}
	#{tags question:_answer.getQuestion(), editable:_extended && _user?.canEdit(_answer.getQuestion()) /}
	
	#{if _user && _answer.getQuestion().owner() != _user && !_user.isBlocked()}
		#{vote entry:_answer.getQuestion(), user:_user /}
	#{/if}
<ul>
<li class="answer #{if _answer.owner() == _user}own#{/if} #{if _answer.isBestAnswer()}bestAnswer#{/if}">
	<h2 id="answer-${_answer.id()}">#{showProfile _answer /}:</h2>
	<p>${_answer.content().raw()}</p>
	#{date _answer /}
	
	#{if _extended && _user}
		<div class="commands">
			#{if !_user.isBlocked() && !_answer.getQuestion().isLocked()}
				<a href ="@{Application.commentAnswer(_answer.getQuestion().id(), _answer.id())}">Add a new comment</a>
			#{/if}
			#{if _user.canEdit(_answer) && !_answer.getQuestion().isLocked()}
				| <a href="@{Secured.deleteAnswer(_answer.getQuestion().id(), _answer.id())}">Delete</a>
			#{/if}
			#{if _user.canEdit(_answer.getQuestion()) && !_answer.getQuestion().isLocked() && _answer.getQuestion().isBestAnswerSettable() && _answer.getQuestion().getBestAnswer() != _answer}
				| <a href="@{Secured.selectBestAnswer(_answer.getQuestion().id(), _answer.id())}#answer-${_answer.id()}">Select as Best</a>
			#{/if}
		</div>
	#{/if}
	#{if _user && _answer.owner() != _user && !_user.isBlocked()}
		#{vote entry:_answer, user:_user /}
	#{/if}
</li>
#{if _extended}
	#{comments entry:_answer, user:_user /}
#{/if}
</ul></li>