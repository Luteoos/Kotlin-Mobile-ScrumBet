Pod::Spec.new do |spec|
    spec.name                     = 'coreSwift'
    spec.version                  = '1.0'
    spec.homepage                 = 'Link to a Kotlin/Native module homepage'
    spec.source                   = { :git => "Not Published", :tag => "Cocoapods/#{spec.name}/#{spec.version}" }
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'Some description for a Kotlin/Native module'
    spec.module_name              = "coreSwift"
    
    spec.ios.deployment_target  = '14.0'
    spec.macos.deployment_target = '13.0'
    spec.static_framework         = false
    spec.dependency 'core'
    spec.source_files = "build/cocoapods/framework/coreSwift/**/*.{h,m,swift}"
end