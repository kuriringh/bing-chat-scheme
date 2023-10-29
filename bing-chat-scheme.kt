class Environment(val outer: Environment?) {
		private val table = HashMap<String, Any>()

		operator fun get(key: String): Any? {
				if (table.containsKey(key)) {
						return table[key]
				} else if (outer != null) {
						return outer[key]
				} else {
						throw RuntimeException("Undefined symbol $key")
				}
		}

		operator fun set(key: String, value: Any) {
				table[key] = value
		}
}


fun eval(x: Any, env: Environment): Any {
    return when (x) {
        is String -> env[x] ?: throw RuntimeException("Undefined symbol $x")
        is Number -> x
        is List<*> -> {
            val op = x[0]
            when (op) {
                "define" -> {
                    val variable = x[1] as String
                    val expression: Any = x[2] as Any
                    val result: Any = eval(expression, env)
                    env[variable] = result
                }
                else -> throw RuntimeException("unknown procedure $op")
            }
        }
        else -> throw RuntimeException("cannot eval $x")
    }
}

fun main() {
		val env = Environment(null)
		eval(listOf("define", "x", 10), env)
		println(env["x"])
}

