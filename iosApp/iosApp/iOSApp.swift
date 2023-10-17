import core
import SwiftUI
#if os(iOS)
    import FirebaseAnalytics
    import FirebaseCore
    import AppTrackingTransparency
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
        authController = AuthController(preferences: nil, serverRepository: nil, buildConfig: nil, applicationVersion: nil)
        authObject = AuthObject(controller: authController)
        #if os(iOS)
            delegate.setAuthControler(authController)
        #endif
    }

    var body: some Scene {
        WindowGroup {
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
                .onReceive(NotificationCenter.default.publisher(for: UIApplication.didBecomeActiveNotification)) { _ in
                    //only works when delayed
                    KHandler().postDelayed(function: {
                        guard #available(iOS 14, *) else {
                            return
                        }
                        let prefs: SharedPreferences = SharedPreferencesImpl()
                        ATTrackingManager.requestTrackingAuthorization { status in
                            switch status{
                            case .authorized:
                                if let app = FirebaseApp.app(){
                                    return
                                }
                                FirebaseApp.configure()
                                Analytics.setAnalyticsCollectionEnabled(true)
                                Analytics.setUserID(prefs.getUserAnalyticsId())
                            default:
                                print("no_tracking")
                                Analytics.setAnalyticsCollectionEnabled(false)
                            }
                        }
                    }, time: 200)
                }
                .onAppear {
                }
//                    todo move to settings
//                    #if os(macOS)
//                    // empty - default dark icon
//                    #else
//                    DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
//                        if UITraitCollection.current.userInterfaceStyle == .light {
//
//                            UIApplication.shared.setAlternateIconName("AppIcon")
//                        } else {
//                            UIApplication.shared.setAlternateIconName(nil)
//                        }
//                    }
//                    #endif
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
        }
    }
#endif
