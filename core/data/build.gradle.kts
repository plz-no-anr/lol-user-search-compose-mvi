plugins {
    id("lol.android.library")
    id("lol.android.hilt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.plznoanr.lol.core.data"

}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.paging.runtime)

    implementation(libs.timber)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext.junit)
//
//    implementationUnitTest()
//
//    implementationAndroidTest()
}