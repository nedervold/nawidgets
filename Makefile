.PHONY: run
run :
	mvn package
	cp ./target/nawidgets-0.1.jar .
	java -jar nawidgets-0.1.jar


.PHONY: clean
clean :
	find . -name '*~' -delete
	find . -name '#*' -delete
	mvn clean
