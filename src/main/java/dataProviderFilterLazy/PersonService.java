package dataProviderFilterLazy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import dataGridList.PersonModel;



public class PersonService {

	private static String[] firstName = { "James", "John", "Robert", "Michael", "William", "David", "Richard",
			"Charles", "Joseph", "Christopher", "Mary", "Patricia", "Linda", "Barbara", "Elizabeth", "Jennifer",
			"Maria", "Susan", "Margaret", "Dorothy", "Lisa" };

	private static String[] lastName = { "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson",
			"Moore", "Taylor", "Andreson", "Thomas", "Jackson", "White" };

	private static List<PersonModel> persons;

	public Stream<PersonModel> getPersons(int offset, int limit, AgeGroup filter) {
		ensureTestData();

		final Stream<PersonModel> filtered = persons.stream().filter(p -> filter(p, filter)).skip(offset).limit(limit);

		return filtered;
	}

	public int totalSize(){
		ensureTestData();
		return persons.size();
	}

	public Collection<PersonModel> findUsers(int start, int end){
		return persons.subList(start, end);
	}

	private void ensureTestData() {
		if (persons == null) {

			final Random r = new Random();

			persons = new ArrayList<PersonModel>();
			for (int i = 0; i < 10000; i++) {
				final PersonModel person = new PersonModel();
				person.setName(firstName[r.nextInt(firstName.length)] + " " + lastName[r.nextInt(lastName.length)]);
				person.setAge(r.nextInt(50) + 18);
				person.setEmail(person.getName().replaceAll(" ", ".") + "@example.com");

				LocalDate date = LocalDate.now().minusYears(person.getAge());
				date = date.withMonth(r.nextInt(12) + 1);
				date = date.withDayOfMonth(r.nextInt(28) + 1);
				person.setBirthday(date);

				persons.add(person);
			}
		}
	}

	private boolean filter(PersonModel p, AgeGroup filter) {
		if (filter == null) {
			return true;
		}

		final int age = p.getAge();
		return filter.getMinAge() <= age && filter.getMaxAge() >= age;
	}

	public int countPersons(int offset, int limit, AgeGroup filter) {
		ensureTestData();

		final long count = persons.stream().filter(p -> filter(p, filter)).skip(offset).limit(limit).count();

		return (int) count;
	}

}

