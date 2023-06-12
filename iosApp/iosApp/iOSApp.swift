import SwiftUI
import core
import FirebaseCore

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    //    way to do DI via @EnvironmentObject
    let authObject : AuthObject

    init(){
        // IOSApp is invoked also for PreviewProviders
        KoinKt.doInitKoin()
        // must be after .doInitKoin due to underlying inject()
        //authObject = ObservableObject(controller: KController())
        authObject = AuthObject(controller:
//                                    MockAuthControllerInterface())
                                    AuthController(
                                       preferences: nil,
                                       serverRepository: nil,
                                       applicationVersion: nil) )
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
      print("dev.luteoos.scrumbet is starting up. ApplicationDelegate didFinishLaunchingWithOptions.")
      FirebaseApp.configure()
      launchOptions?.forEach({ (key: UIApplication.LaunchOptionsKey, value: Any) in
          print(key.rawValue, value)
            })
    return true
  }
}
