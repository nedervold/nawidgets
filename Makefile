V=0.1.2

.PHONY: run
run :
	mvn package
	cp ./target/nawidgets-$(V).jar .
	java -jar nawidgets-$(V).jar


.PHONY: clean
clean :
	-rm *.jar
	find . -name '*~' -delete
	find . -name '#*' -delete
	mvn clean
