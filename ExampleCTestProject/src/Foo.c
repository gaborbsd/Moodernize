int globalIntFromFoo = 122;

typedef struct Person {
	char name[20];
	int age;
}Person;

typedef struct Car {
	double mass;
	long price;
	Person owner;
}Car;

struct someStruct {

};

int someFunction(int intParam[], char charParam) {
	return intParam[0] * charParam;
}
char f(Person person) {
	return 'a';
}
int g(Person person, Car car) {
	return 0;
}
Car h() {
	Car c;
	return c;
}
Person j(Person person) {
	return person;
}
Person k (Car k) {
	Person p;
	return p;
}

struct someStruct l(Car car, Person person) {
	struct someStruct a;
	return a;
}

Car m(Car car, Person person, struct someStruct s) {
	return car;
}

