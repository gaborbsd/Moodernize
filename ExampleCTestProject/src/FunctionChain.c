void b() {

}

void a() {
	b();
}

void c() {
	b();
}

void d() {
	b();
}

void e() {
	a();
	c();
	b();
}

void y() {
	x();
}

void x() {
	y();
}

void q() {
	e();
}

