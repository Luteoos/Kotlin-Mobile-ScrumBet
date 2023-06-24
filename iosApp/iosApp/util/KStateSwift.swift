// based on generation by moko-kswift plugin
import core

/**
 * Swift implementation of KState */
public enum KStateSwift<T: AnyObject, E: AnyObject> {
    case empty
    case error(KStateError<E>)
    case loading
    case success(KStateSuccess<T>)

    public var sealed: KState {
        switch self {
        case .empty:
            return core.KStateEmpty() as core.KState
        case let .error(obj):
            return obj as core.KState
        case .loading:
            return core.KStateLoading() as core.KState
        case let .success(obj):
            return obj as core.KState
        }
    }

    public init(_ obj: KState) {
        if obj is core.KStateEmpty {
            self = .empty
        } else if let obj = obj as? core.KStateError<E> {
            self = .error(obj)
        } else if obj is core.KStateLoading {
            self = .loading
        } else if let obj = obj as? core.KStateSuccess<T> {
            self = .success(obj)
        } else {
            fatalError("KStateSwift not synchronized with KState class")
        }
    }
}
