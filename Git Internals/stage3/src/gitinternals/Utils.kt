package gitinternals

fun log(message: String){
    // System.err is not checked by tests, which is good for logging
    System.err.println(message);
}

fun log(message: Any) {
    System.err.println(message.toString())
}