listView('PROJECT TEST') {
    description('All test jobs')
    jobs {
        name('testmavenjob')
    }
    columns {
        status()
        name()
        lastSuccess()
        lastDuration()
        jacoco()
    }
}