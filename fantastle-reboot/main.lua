local push = require "lib/push"

function love.load()
    -- Set up screen
    local gameWidth, gameHeight = 1080, 720
    local windowWidth, windowHeight = love.window.getDesktopDimensions()
    push:setupScreen(gameWidth, gameHeight, windowWidth, windowHeight, {fullscreen = true, resizable = true, highdpi = true, pixelperfect = false, canvas = false, stretched = true})
    -- Cache UI assets
    ui_images = {
        logo = love.graphics.newImage("assets/images/ui/logo.png")
    }
    -- Cache the music for later
    musics = {
        title = love.audio.newSource("assets/music/title.ogg", "stream")
    }
    -- Set music options
    musics.title:setLooping(true)
    -- Set up joysticks
    local joysticks = love.joystick.getJoysticks()
    local joycount = love.joystick.getJoystickCount()
    if joycount >= 1 then
        joystick = joysticks[1]
    end
    -- Play title music
    love.audio.play(musics.title)
end

function love.draw()
    push:start()
    love.graphics.draw(ui_images.logo)
    love.graphics.print({{0, 0, 0, 1}, "Press ESCAPE to quit"}, 400, 340)
    if joystick then
        love.graphics.print({{0, 0, 0, 1}, "Joystick detected; button 9 also quits"}, 400, 360)
    end
    push:finish()
end

function love.resize(w, h)
    return push:resize(w, h)
end

function love.update(dt)
    if love.keyboard.isScancodeDown("escape") then
        love.audio.stop()
        love.event.quit(0)
    end
    if joystick then
        if joystick:isDown(9) then
            love.audio.stop()
            love.event.quit(0)
        end
    end
end