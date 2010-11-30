#{if false} Arguments: _entry, _user #{/if}
#{if _entry.comments().size() != 0}
	<ul class="comments">
		#{list items:_entry.comments(), as:'comment'}
			<li class="comments">
				<p id="comment-${comment.id()}">#{showProfile comment /}:
				${comment.content()}</p>
				#{date comment /}
				<!-- by team2 -->
				<p align="right">
		 		#{if _user && comment.owner() != _user && !_user.isBlocked() && !comment.getLikers().contains(_user)}
					<img alt="I like" src="/public/images/thumbUp.png" />
					#{if _entry instanceof models.Answer}
						<a href="@{Secured.addLikerAnswerComment(comment.id(), _entry.getQuestion().id(), _entry.id())}#comment-${comment.id()}">I like</a>
					#{/if}#{else}
						<a href="@{Secured.addLikerQuestionComment(comment.id(), _entry.id())}#comment-${comment.id()}">I like</a>
					#{/else }
				#{/if}
				#{if _user && comment.owner() != _user && !_user.isBlocked() && comment.getLikers().contains(_user)}
					<img alt="I don't like" src="/public/images/thumbDown.png" />
					#{if _entry instanceof models.Answer}
						<a href="@{Secured.removeLikerAnswerComment(comment.id(), _entry.getQuestion().id(), _entry.id())}#comment-${comment.id()}">I don't like</a>
					#{/if}#{else}
						<a href="@{Secured.removeLikerQuestionComment(comment.id(), _entry.id())}#comment-${comment.id()}">I don't like</a>
					#{/else }
				#{/if}
				<!-- count of users who like this -->
				#{if comment.countLikers()==1 }
				1 user likes this
				#{/if}
				#{if comment.countLikers()>1 }
				${comment.countLikers()} users like this
				#{/if}
				</p>
				<!-- EOT2 = end of team2 -->
				#{if _user && _user.canEdit(comment)}
					#{if _entry instanceof models.Answer}
						<a href="@{Secured.deleteCommentAnswer(_entry.getQuestion().id(), _entry.id(), comment.id())}#answer-${_entry.id()}">Delete</a>
					#{/if}#{else}
						<a href="@{Secured.deleteCommentQuestion(_entry.id(), comment.id())}">Delete</a>
					#{/else}
				#{/if}
			</li>	
		#{/list}
	</ul>
#{/if}
