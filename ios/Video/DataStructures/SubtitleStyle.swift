struct SubtitleStyle {
    // Extend with more style properties as needed.
    private(set) var opacity: CGFloat
    private(set) var topPositionRate: CGFloat
    private(set) var resolution: CGSize

    enum SubtitleStyleKeys {
        static let opacity = "opacity"
        static let topPositionRate = "topPositionRate"
        static let resolution = "resolution"
    }

    init(opacity: CGFloat, topPositionRate: CGFloat, resolution: CGSize) {
        self.opacity = opacity
        self.topPositionRate = topPositionRate
        self.resolution = resolution
    }

// 편의상 해상도 resolution도 여기서 설정
    static func parse(from dictionary: [String: Any]?) -> SubtitleStyle {
        // dictionary의 내용을 출력
        if let dict = dictionary {
            // print("플레이어아이템 자막 스타일 딕셔너리 적용 \(dict)")
        } else {
            // print("플레이어아이템 자막 스타일 딕셔너리 적용: nil")
        }
        let opacity = dictionary?[SubtitleStyleKeys.opacity] as? CGFloat ?? 1
        let topPositionRate = dictionary?[SubtitleStyleKeys.topPositionRate] as? CGFloat ?? 0
        var resolution = CGSize(width: 1080, height: 1920) // 기본 값
    if let resolutionDict = dictionary?[SubtitleStyleKeys.resolution] as? [String: CGFloat],
       let width = resolutionDict["width"],
       let height = resolutionDict["height"] {
        resolution = CGSize(width: width, height: height)
    }

        return SubtitleStyle(opacity: opacity, topPositionRate: topPositionRate, resolution: resolution)
    }
}
