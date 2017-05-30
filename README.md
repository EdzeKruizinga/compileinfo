# CompileInfo

This library contains the CompileInfo annotation which provides a convenient way to get compile time information in a [Java][java] application.

More specifically it is an annotation processor that captures the time and system properties at compile time and stores them in a generated Java class. This is helpful for devops purposes or in any delivery pipeline. It allows developers, operations and testers to know exactly when and where an application was built if these properties are exposed by the application at runtime.

## Usage

To use in a Maven project:

In order to activate metadata generation you will need to include 
`compileinfo-${version}.jar` in your build at compile time.

In a Maven project, one would include the `net.q2ek:compileinfo` artifact as a "compile" dependency:

```xml
<dependency>
	<groupId>net.q2ek</groupId>
	<artifactId>compileinfo</artifactId>
	<version>0.10.0</version>
	<scope>compile</scope>
</dependency>
```

To support annotation processing in your IDE see:
https://immutables.github.io/apt.html

### Example

```java
import net.q2ek.compileinfo.CompileInfo;

@CompileInfo
class MyClass {
	public String compileDateTime() {
		return MyClassCompileInfo.localDateTime().toString();
	}

	public String jenkinsBuildUrl() {
		return MyClassCompileInfo.get("env.BUILD_URL");
	}
}
```

## Built With

* [AutoService](https://github.com/google/auto/tree/master/service) - Google Auto Service
* [Maven](https://maven.apache.org/) - Dependency Management

The project is a standard Maven project.

## Versioning

I use [SemVer](http://semver.org/) for versioning.

### Releases

* 0.10.0 is available at Maven Central

## Authors

* [EdzeKruizinga](https://github.com/EdzeKruizinga) - *Initial work*

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE.md](LICENSE.md) file for details

[java]: https://en.wikipedia.org/wiki/Java_(programming_language)
