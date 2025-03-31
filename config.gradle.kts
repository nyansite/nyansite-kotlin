extra["prepareConfig"] = fun () {
    var databaseKeystorePropertiesFileName = "database-keystore.properties"

    if (buildVariant == BuildVariants.AOT.value || buildVariant == BuildVariants.NATIVE.value) {
        databaseKeystorePropertiesFileName = "database-keystore.properties"
    } else if (buildVariant == BuildVariants.TEST.value) {
        databaseKeystorePropertiesFileName = "test-database-keystore.properties"
    }

    val databaseKeystorePropertiesFile = rootProject.file(databaseKeystorePropertiesFileName)
    val databaseKeystoreProperties = Properties()
    keystoreProperties.add(databaseKeystoreProperties)

    try {
        FileInputStream(databaseKeystorePropertiesFile).use { inputStream ->
            databaseKeystoreProperties.load(inputStream)
        }
    } catch (ignored: Exception) {
    }
}

extra["loadConfig"] = fun () {
    tasks.withType<ProcessResources> {
        with(
            copySpec {
                duplicatesStrategy = DuplicatesStrategy.INCLUDE

                from("src/main/resources")
                include("**/application.yml")
                keystoreProperties.forEach() { props ->
                    props.forEach() { prop ->
                        if (prop.value != null) {
                            filter { it.replace("@${prop.key.toString()}@", prop.value.toString()) }
                            filter { it.replace("@project.${prop.key.toString()}@", prop.value.toString()) }
                        }
                    }
                }
            }
        )
    }
}