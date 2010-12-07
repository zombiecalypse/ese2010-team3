package tests;

import java.util.Collections;

import models.Answer;
import models.ISystemInformation;
import models.Notification;
import models.Question;
import models.SystemInformation;
import models.User;
import models.helpers.IObservable;
import models.helpers.IObserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;
import tests.mocks.SystemInformationMock;

public class NotificationTest extends UnitTest {

	private ISystemInformation savedSysInfo;
	private Question question;
	private User norbert;
	private User andrew;

	@Before
	public void setUp() {
		savedSysInfo = SystemInformation.get();
		norbert = new User("Norbert", "norbert");
		question = new Question(norbert, "Need I be watched?");
		andrew = new User("Andrew", "andrew");
	}

	@After
	public void tearDown() {
		SystemInformation.mockWith(savedSysInfo);
	}

	@Test
	public void shouldBeObserving() {
		assertFalse(norbert.isObserving(question));
		norbert.startObserving(question);
		assertTrue(norbert.isObserving(question));
		norbert.stopObserving(question);
		assertFalse(norbert.isObserving(question));
	}

	@Test
	public void shouldBeNotified() {
		assertEquals(norbert.getNotifications().size(), 0);
		question.answer(andrew, "Answer one");
		assertEquals(norbert.getNotifications().size(), 0);
		norbert.startObserving(question);
		Answer answer2 = question.answer(andrew, "Answer two");
		assertEquals(norbert.getNotifications().size(), 1);
		assertEquals(norbert.getNotifications().get(0).getAbout(), answer2);
		norbert.getNotifications().get(0).unregister();
		assertEquals(norbert.getNotifications().size(), 0);
		question.answer(andrew, "Answer three");
		assertEquals(norbert.getNotifications().size(), 1);
		norbert.stopObserving(question);
		question.answer(andrew, "Answer four");
		assertEquals(norbert.getNotifications().size(), 1);
	}

	@Test
	public void shouldHaveRecentNotifications() {
		SystemInformationMock sys = new SystemInformationMock();
		SystemInformation.mockWith(sys);
		sys.hour(12).minute(0);

		norbert.startObserving(question);
		assertNull(norbert.getVeryRecentNewNotification());
		assertEquals(norbert.getNewNotifications().size(), 0);

		question.answer(andrew, "recent answer?");
		Notification recent = norbert.getVeryRecentNewNotification();
		assertNotNull(recent);
		assertEquals(recent.getAbout().summary(), "recent answer?");

		sys.minute(2);
		assertNotNull(norbert.getVeryRecentNewNotification());
		assertEquals(norbert.getVeryRecentNewNotification(), recent);
		sys.minute(10);
		assertNull(norbert.getVeryRecentNewNotification());

		assertTrue(recent.isNew());
		assertEquals(norbert.getNewNotifications().size(), 1);
		recent.unsetNew();
		assertFalse(recent.isNew());
		assertEquals(norbert.getNewNotifications().size(), 0);
	}

	@Test
	public void shouldHaveDifferentNotificationIDs() {
		norbert.startObserving(question);
		for (int i = 0; i < 10; i++) {
			question.answer(andrew, "Answer " + i);
		}
		assertEquals(norbert.getNotifications().size(), 10);

		Notification first = Collections.max(norbert.getNotifications());
		assertEquals(norbert.getNotification(first.id()), first);
		assertEquals(norbert.getNotifications().get(9), first);

		for (int i = 0; i < 9; i++) {
			assertTrue(norbert.getNotifications().get(i).id() > norbert
					.getNotifications().get(i + 1).id());
		}

		Notification last = Collections.min(norbert.getNotifications());
		assertEquals(norbert.getNotifications().get(0), last);
		assertEquals(norbert.getVeryRecentNewNotification(), last);

		last.unsetNew();
		assertEquals(norbert.getVeryRecentNewNotification(), norbert
				.getNotifications().get(1));

		assertNull(norbert.getNotification(-1));
	}

	@Test
	public void shouldNotGetSelfNotified() {
		norbert.startObserving(question);
		andrew.startObserving(question);
		question.answer(norbert, "Norbert's answer");
		question.answer(andrew, "Andrew's answer");
		assertEquals(norbert.getNotifications().size(), 1);
		assertEquals(andrew.getNotifications().size(), 1);
		assertEquals(norbert.getVeryRecentNewNotification().getAbout().owner(),
				andrew);
		assertEquals(andrew.getVeryRecentNewNotification().getAbout().owner(),
				norbert);
	}

	@Test
	public void shouldNotNotifyAboutDeletedEntries() {
		norbert.startObserving(question);
		question.answer(andrew, "soon to be gone");
		assertEquals(norbert.getNotifications().size(), 1);
		assertNotNull(norbert.getVeryRecentNewNotification());
		andrew.delete();
		assertEquals(norbert.getNotifications().size(), 0);
		assertNull(norbert.getVeryRecentNewNotification());
	}

	@Test
	public void shouldNotifyAboutAnonymousEntries() {
		norbert.startObserving(question);
		question.answer(andrew, "soon to be gone");
		assertEquals(norbert.getNotifications().size(), 1);
		assertNotNull(norbert.getVeryRecentNewNotification());
		andrew.anonymize(true);
		andrew.delete();
		assertEquals(norbert.getNotifications().size(), 1);
		assertNotNull(norbert.getVeryRecentNewNotification());
		assertNull(norbert.getVeryRecentNewNotification().getAbout().owner());
	}

	@Test
	public void shouldUnregisterNotifications() {
		norbert.observe(question, question.answer(andrew, "???"));
		Notification notification = norbert.getNotifications().get(0);
		assertNotNull(notification.owner());
		norbert.anonymize(true);
		norbert.delete();
		assertNull(notification.owner());
	}

	@Test
	public void shouldOnlyNotifyAboutAnswersForNow() {
		IObservable unobservable = new IObservable() {
			public void addObserver(IObserver o) {
			}
			public void removeObserver(IObserver o) {
			}
			public boolean hasObserver(IObserver o) {
				return false;
			}
			public void notifyObservers(Object arg) {
			}
		};
		norbert.observe(unobservable, null);
		norbert.observe(question, null);
		assertEquals(norbert.getNotifications().size(), 0);

		// keep Cobertura happy
		unobservable.addObserver(norbert);
		unobservable.removeObserver(norbert);
		assertFalse(unobservable.hasObserver(norbert));
		unobservable.notifyObservers(norbert);
	}

	@Test
	public void shouldRequireObserver() {
		boolean hasThrown = false;
		try {
			question.addObserver(null);
		} catch (IllegalArgumentException ex) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}
}
