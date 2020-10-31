package processor

import java.util.*
import kotlin.math.pow

val scanner = Scanner(System.`in`)
var isRun = true
var deter =0.0

fun main() {
    while(isRun) {
        println ("1. Add matrices\n" +
                "2. Multiply matrix by a constant\n" +
                "3. Multiply matrices\n" +
                "4. Transpose matrix\n" +
                "5. Calculate a determinant\n" +
                "6. Inverse matrix\n" +
                "0. Exit")
        print("Your choice: ")
        val options = scanner.next()
        when (options) {
            "0" -> isRun = false
            "1" -> addition()
            "2" -> multiply()
            "3" -> matrixMult()
            "4" -> transpose()
            "5" -> det()
            "6" -> inverse()
        }
    }
}

fun transpose() {
    println ("1. Main diagonal\n" +
            "2. Side diagonal\n" +
            "3. Vertical line\n" +
            "4. Horizontal line\n")
    print("Your choice: ")
    val options = scanner.next()
    when (options) {
        "1" -> transMain()
        "2" -> transSide()
        "3" -> transVert()
        "4" -> transHor()
    }

}

fun transMain() {
    var a = setMatrix()
    var result = Array<Array<Double>>(a.size) { Array<Double>(a[0].size) { 0.0 } }
    for (i in a[0].indices) {
        result[i] = getRow(a, i)
    }
    printMatrix(result)
}

fun transSide() {
    var a = setMatrix()
    var result = Array<Array<Double>>(a.size) { Array<Double>(a[0].size) { 0.0 } }
    for (i in a[0].indices) {
        result[i] = getRow(a, a.lastIndex - i).reversedArray()
    }
    printMatrix(result)
}

fun transVert() {
    var a = setMatrix()
    var result = Array<Array<Double>>(a.size) { Array<Double>(a[0].size) { 0.0 } }
    for (i in a[0].indices) {
        result[i] = getCol(a, i).reversedArray()
    }
    printMatrix(result)
}

fun transHor() {
    var a = setMatrix()
    var result = Array<Array<Double>>(a.size) { Array<Double>(a[0].size) { 0.0 } }
    for (i in a[0].indices) {
        result[i] = getCol(a, a.lastIndex - i)
    }
    printMatrix(result)
}


fun setMatrix(name: String = ""): Array<Array<Double>> {
    println("Enter size of$name matrix")
    val n = scanner.nextInt()
    val m = scanner.nextInt()
    println("Enter$name matrix:")
    val matrix = Array<Array<Double>>(n) { Array<Double>(m) {scanner.nextDouble() } }
    return matrix
}

fun addition() {
    val a = setMatrix(" first")
    val b = setMatrix(" second")
    if (a.size != b.size || a[0].size != b[0].size) {
        println("ERROR")
    } else {
        for (n in a.indices) {
            for (m in a[n].indices) {
                a[n][m] += b[n][m]
            }
        }
    }
    printMatrix(a)
}

fun multiply() {
    val a = setMatrix()
    print("Enter constant:")
    val mult = scanner.next().toDouble()
    for (n in a.indices) {
        for (m in a[n].indices) {
            a[n][m] *= mult
        }
    }
    printMatrix(a)
}

fun matrixMult() {
    val a = setMatrix(" first")
    val b = setMatrix(" second")
    var result = Array(a.size){Array<Double>(b[0].size){0.0}}
    if (a[0].size != b.size) {
        println("ERROR")
    } else {
        for (n in result.indices) {
            for (m in result[n].indices) {
                result[n][m] = multRow(getCol(a, n), getRow(b, m))
            }
        }
        printMatrix(result)
    }
}

fun multRow(a: Array<Double>, b: Array<Double>): Double {
    var res = 0.0
    a.indices.forEach { i -> res += a[i] * b[i] }
    return res
}

fun getRow(a: Array<Array<Double>>, m: Int): Array<Double> {
    var result = Array<Double>(a.size) {0.0}
    for (n in result.indices) {
        result[n] = a[n][m]
    }
    return result
}

fun getCol(a: Array<Array<Double>>, n: Int): Array<Double> {
    var result = Array<Double>(a[0].size) {0.0}
    for (m in result.indices) {
        result[m] = a[n][m]
    }
    return result
}

fun printMatrix (a: Array<Array<Double>>) {
    println("The result is:")
    a.forEach { println(it.joinToString(" ")) }
    println()
}

fun getMinor(matrix: Array<Array<Double>>, n: Int, m: Int): Array<Array<Double>> {
    val size = matrix.size
    var x = 0
    var y = 0
    val minor = Array(size - 1){Array<Double>(size - 1){0.0}}
    for (i in 0 until size) {
        if (i != n) {
            for (j in 0 until size) {
                if (j != m) {
                    minor[x][y] = matrix[i][j]
                    y++
                }
            }
            x++
            y = 0
        }
    }
    return minor
}

fun det() {
    val matrix = setMatrix()
    println("The result is:")
    deter = 0.0
    println(detRec(matrix))
    println()
}

fun detRec(matrix: Array<Array<Double>>): Double {
    val n = matrix.size
    if (n == 2) {
        deter = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]
        return deter
    } else {
        deter = 0.0
        for (i in 0 until n) {
            deter += matrix[0][i] * detRec(getMinor(matrix, 0, i)) * Math.pow(-1.0, i.toDouble())
        }
    }
    return deter
}

fun cofactor(matrix: Array<Array<Double>>): Array<Array<Double>> {
    val n = matrix.size
    val cof = Array(n){Array<Double>(n){0.0}}
    for (i in 0 until n) {
        for (j in 0 until n) {
            cof[i][j] = detRec(getMinor(matrix, i, j)) * (-1.0).pow(i + j.toDouble())
        }
    }
    return cof
}

fun inverse() {
    val matrix = setMatrix()
    val n = matrix.size
    //inverse det
    val detInv: Double = 1 / detRec(matrix)
    //make cofactor matrix
    val cof = cofactor(matrix)
    //transpose
    val cofT = Array(n){Array<Double>(n){0.0}}
    for (i in 0 until n) {
        for (j in 0 until n) {
            cofT[j][i] = cof[i][j]
        }
    }
    //multiply
    for (i in 0 until n) {
        for (j in 0 until n) {
            cofT[i][j] *= detInv
        }
    }
    //print
    printMatrix(cofT)
}