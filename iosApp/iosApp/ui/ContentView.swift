import SwiftUI
import core

struct ContentView: View {
	let greet = Greeting().greeting()
    let cont : UserControllerInterface =
    MockUserController()
    @State var text: String = ""
//    let cont : UserControllerInterface
//    let state: UserData

	var body: some View {
        VStack{
            Text(greet)
//            Text(cont.test())
            Button("Click ME!", action: {
                cont.watchState().watch(block: {
                    s in
                    let state = KStateSwift<AnyObject, AnyObject>(s)
                    switch state {
                    case .empty:
                        ""
                    case .error(let kStateError):
                        ""
                    case .loading:
                        ""
                    case .success(let kStateSuccess):
                        text = (kStateSuccess.value?.description())!
                    }
                })
                cont.updateUsername(username: "test")//getUserData()
//                text = cont.test()
            })
            Text(text)
        }
//        cont.watchState()
//        cont.watchState()
//        cont.watchState()
//        cont.watchState().watch { state in
//            self.state = state
//            self.state
//            let s = state as! KState
//            s.data()
//        }
//        test.watchState().watch { [weak self] state in
//            guard let state = state as? KState<UserData, AppException> else {}
//            state.data()?.userId
//            Text("sad")
//        }
	}
}

struct ContentView_Previews: PreviewProvider {
//    init(){
//        KoinKt.doInitKoin()
//    }
	static var previews: some View {
        ContentView()
            .previewLayout(.sizeThatFits)
            .preferredColorScheme(.dark)
	}
}

class MockUserController : UserControllerInterface {
    
    func updateUsername(username: String) {
        print("update username", username)
    }
    
    func onDeInit() {
        print("deinit")
    }
    
    func onStart() {
        print("start")
    }
    
    func onStop() {
        print("stop")
    }
    
    func watchState() -> CFlow<KState> {
        let mockValue = KStateSuccess(value: "mockValue" as AnyObject )
        return CFlowCompanion().getMock(mockValue: mockValue as KState) as! CFlow<KState>
    }
    
    
}
