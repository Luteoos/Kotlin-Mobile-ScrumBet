import SwiftUI
import core

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    //    way to do DI via @EnvironmentObject
    let authObject : MockObservableObject

    init(){
        KoinKt.doInitKoin()
        // must be after .doInitKoin due to underlying inject()
        //authObject = ObservableObject(controller: KController())
        authObject = MockObservableObject()
    }

	var body: some Scene {
		WindowGroup {
            AppView()
                .environmentObject(authObject)
        }
	}

}

class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
      print("dev.luteoos.template is starting up. ApplicationDelegate didFinishLaunchingWithOptions.")
     launchOptions?.forEach({ (key: UIApplication.LaunchOptionsKey, value: Any) in
         print(key.rawValue, value)
        })
    return true
  }
}
