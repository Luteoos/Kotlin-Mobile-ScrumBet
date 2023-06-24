import core
import SwiftUI
#if os(iOS)
    import FirebaseAnalytics
    import FirebaseCore
#endif

@main
struct iOSApp: App {
    #if os(iOS)
        @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    #endif

    //    way to do DI via @EnvironmentObject
    let authObject: AuthObject
    private let authController: AuthControllerInterface

    init() {
        // IOSApp is invoked also for PreviewProviders
        print("app Init")
        KoinKt.doInitKoin()
        // must be after .doInitKoin due to underlying inject()
        // authObject = ObservableObject(controller: KController())
        authController = AuthController(preferences: nil, serverRepository: nil, applicationVersion: nil)
        authObject = AuthObject(controller: authController)
        #if os(iOS)
            delegate.setAuthControler(authController)
        #endif
    }

    var body: some Scene {
        WindowGroup {
            // #if os(iOS)
            AppView()
                .environmentObject(authObject)
                .environment(\.authController, authController)
                .onOpenURL { url in
                    print(url)
                    guard let roomId = NSURLComponents(url: url, resolvingAgainstBaseURL: true)?.path
                    else {
                        return print("invalid URL \(url)")
                    }
                    authController.setRoomConnectionId(id: roomId)
                }
//            #else
//            NavigationWrapper {
//                ContentView()
//            }
//                .environmentObject(authObject)
//                .environment(\.authController, authController)
//            #endif
        }
    }
}

#if os(iOS)
    class AppDelegate: NSObject, UIApplicationDelegate {
        private var authController: AuthControllerInterface? = nil

        func setAuthControler(_ controler: AuthControllerInterface) {
            authController = controler
        }

        func application(_ application: UIApplication,
                         didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool
        {
            startup(application)
//      print("ApplicationDelegate didFinishLaunchingWithOptions")
            launchOptions?.forEach { (key: UIApplication.LaunchOptionsKey, value: Any) in
                print(key.rawValue, value)
            }
            return true
        }

        // not used
        func application(_ app: UIApplication,
                         open url: URL,
                         options _: [UIApplication.OpenURLOptionsKey: Any] = [:]) -> Bool
        {
            startup(app)
            guard let components = NSURLComponents(url: url, resolvingAgainstBaseURL: true),
                  let pathRoomId = components.path
            else {
                return false
            }
            authController!.setRoomConnectionId(id: pathRoomId)
            return true
        }

        private func startup(_ app: UIApplication) {
            print("dev.luteoos.scrumbet is starting up. Application \(app.description)")
            let prefs: SharedPreferences = SharedPreferencesImpl()
            FirebaseApp.configure()
            #if DEBUG
                Analytics.setUserID(prefs.getUserAnalyticsId())
            #endif
        }
    }
#endif
