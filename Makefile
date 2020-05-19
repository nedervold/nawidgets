.PHONY: run
run :
	mvn package
	java -jar ./target/nawidgets-1.0.jar


.PHONY: clean
clean :
	find . -name '*~' -delete
	find . -name '#*' -delete
	mvn clean
