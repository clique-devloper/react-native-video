struct SubtitleStyle {
    // Extend with more style properties as needed.
    private(set) var opacity: CGFloat
    private(set) var topPositionRate: CGFloat

    enum SubtitleStyleKeys {
        static let opacity = "opacity"
        static let topPositionRate = "topPositionRate"
    }

    init(opacity: CGFloat, topPositionRate: CGFloat) {
        self.opacity = opacity
        self.topPositionRate = topPositionRate
    }

    static func parse(from dictionary: [String: Any]?) -> SubtitleStyle {
        // dictionary의 내용을 출력
        if let dict = dictionary {
            // print("플레이어아이템 자막 스타일 딕셔너리 적용 \(dict)")
        } else {
            // print("플레이어아이템 자막 스타일 딕셔너리 적용: nil")
        }
        let opacity = dictionary?[SubtitleStyleKeys.opacity] as? CGFloat ?? 1
        let topPositionRate = dictionary?[SubtitleStyleKeys.topPositionRate] as? CGFloat ?? 0

        return SubtitleStyle(opacity: opacity, topPositionRate: topPositionRate)
    }
}
