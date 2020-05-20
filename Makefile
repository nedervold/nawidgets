.PHONY: run
run :
	mvn package
	java -jar ./target/nawidgets-0.1.jar


.PHONY: clean
clean :
	find . -name '*~' -delete
	find . -name '#*' -delete
	mvn clean
