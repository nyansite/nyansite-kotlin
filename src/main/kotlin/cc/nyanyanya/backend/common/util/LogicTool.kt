package cc.nyanyanya.backend.common.util

import cc.nyanyanya.backend.common.util.bo.DefaultValue

object LogicTool {
    @Throws(IllegalArgumentException::class)
    fun isJustOneNotNull(args: List<Any>, defaultValues: List<Any>): result {
        if (args.size != defaultValues.size) {
            throw IllegalArgumentException("args and defaultValues must have the same size")
        }

        val logicExpressions = mutableListOf<Boolean>()
        for (i in args.indices) {
            logicExpressions.add(args[i] != defaultValues[i])
        }

        return isJustOneTrue(logicExpressions)
    }

    fun isJustOneTrue(args: List<Boolean>): result {
        val result = result()

        var trueCount = 0
        args.forEachIndexed({ index, arg ->
            if (arg) {
                trueCount++
                result.trueIndex = index
            }
        })

        if (trueCount != 1) {
            result.trueIndex = DefaultValue.DEFAULT_INT
            result.isTrue = false
        }

        return result
    }
}

class result {
    var isTrue = true
    var trueIndex = DefaultValue.DEFAULT_INT

    override fun toString(): String {
        return "result(isTrue=$isTrue, trueIndex=$trueIndex)"
    }
}