-- assets
local assets = {
    images = {
        avatars = {},
        boss = {},
        effects = {},
        monsters = {},
        objects = {},
        ui = {}
    },
    music = {},
    sounds = {}
}

function love.load()
    -- load libraries
    push = require "lib.push"
    loveframes = require "lib.loveframes"
    -- Set up screen
    local gameWidth, gameHeight = 1080, 720
    local windowWidth, windowHeight = love.window.getDesktopDimensions()
    push:setupScreen(gameWidth, gameHeight, windowWidth, windowHeight, {fullscreen = true, resizable = true, highdpi = true, pixelperfect = false, canvas = false, stretched = true})
    -- Set up joysticks
    local joysticks = love.joystick.getJoysticks()
    local joycount = love.joystick.getJoystickCount()
    if joycount >= 1 then
        joystick = joysticks[1]
    end
    -- Cache logo image
    assets.images.ui.logo = love.graphics.newImage("assets/images/ui/logo.png")
    -- Cache title screen music
    assets.music.title = love.audio.newSource("assets/music/title.ogg", "stream")
    -- Set music options
    assets.music.title:setLooping(true)
    -- Play title music
    love.audio.play(assets.music.title)
end

function love.resize(w, h)
    return push:resize(w, h)
end

function love.draw()
    push:start()
    love.graphics.draw(assets.images.ui.logo)
    love.graphics.print({{0, 0, 0, 1}, "Press ESCAPE to quit"}, 400, 340)
    if joystick then
        love.graphics.print({{0, 0, 0, 1}, "Joystick detected; button 9 also quits"}, 400, 360)
    end
    loveframes.draw()
    push:finish()
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
    loveframes.update(dt)
end

function love.mousepressed(x, y, button)
    loveframes.mousepressed(x, y, button)
end
 
function love.mousereleased(x, y, button)
    loveframes.mousereleased(x, y, button)
end
 
function love.keypressed(key, unicode)
    loveframes.keypressed(key, unicode)
end
 
function love.keyreleased(key)
    loveframes.keyreleased(key)
end