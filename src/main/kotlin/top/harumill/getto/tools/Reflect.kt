package top.harumill.getto.tools


fun initObject(className: String) {
    val fClass = Class.forName(className)
    val const = fClass.declaredConstructors
    const.forEach {
        try {
            it.newInstance()
        } catch (e: Exception) {
            //
        }
    }
}

fun getObjectByName(className: String): Any? {
    val fClass = Class.forName(className).kotlin
    return fClass.objectInstance
}