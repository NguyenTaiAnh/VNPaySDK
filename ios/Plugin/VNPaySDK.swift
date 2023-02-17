import Foundation

@objc public class VNPaySDK: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
